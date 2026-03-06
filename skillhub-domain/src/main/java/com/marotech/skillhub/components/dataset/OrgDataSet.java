package com.marotech.skillhub.components.dataset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.marotech.skillhub.components.config.Config;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.*;
import com.marotech.skillhub.repository.GenericRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Transactional
@Component
@DependsOn("baseDataSet")
public class OrgDataSet {
    private boolean isDev = false;

    @PostConstruct
    public void create() {
        try {
            String env = config.getProperty("env.deployment");
            if ("dev".equalsIgnoreCase(env)) {
                isDev = true;
            }
            String ext = config.getProperty("current.country.initials");

            String adminEmail = "system_admin@skillhub.co." + ext;
            long num = repositoryService.countUsers();
            if (num > 0) {
                return;
            }

            User superAdmin = null;
            Iterable<UserRole> roles = repositoryService.findAllRoles();

            if (superAdmin == null) {
                String country = config.getProperty("country");
                AuthUser authUser = new AuthUser();
                authUser.setUserName("system_admin@skillhub.co." + ext);
                String newPassword = AuthUser.encodedPassword("test");
                authUser.setPassword(newPassword);

                repository.save(authUser);
                superAdmin = new User();
                superAdmin.setVerified(Verified.YES);
                superAdmin.setNationalId("12345001");

                superAdmin.setFirstName("System Admin");
                superAdmin.setLastName("System Admin");
                superAdmin.setMiddleName("System Admin");
                superAdmin.setEmail(adminEmail);

                City city = repositoryService.fetchCityByName("Harare", "Zimbabwe");
                if(city == null){
                    city = new City();
                    city.setName("Harare");
                    city.setCountry("Zimbabwe");
                    repositoryService.save(city);
                }
                Suburb suburb = repositoryService.fetchSuburbByName(city, "City Center");
                if(suburb == null) {
                    suburb = new Suburb();
                    suburb.setCity(city);
                    suburb.setName("City Center");
                    repositoryService.save(suburb);
                }

                Address address = new Address();
                address.setSuburb(suburb);
                address.setAddress("80 Samora Machel Avenue");
                repositoryService.save(address);
                superAdmin.setAddress(address);
                superAdmin.setMobilePhone("0712374658");
                repository.save(superAdmin);


                for (UserRole role : roles) {
                    if (role.getRoleName().equals("System Administrator")) {
                        superAdmin.addUserRole(role);
                        break;
                    }
                }
                repository.save(superAdmin);
                authUser.setUser(superAdmin);
                repository.save(authUser);
            }
            createProfilesFromFile(roles);
            //createDefaultUsers();
        } catch (Exception e) {
            LOG.error("Error building basic data :", e);
            System.exit(0);
        }
    }

    private void createProfilesFromFile(Iterable<UserRole> roles) throws IOException {
        List<SkillProvider> providers = parseArrayFromFile();
        String country = config.getProperty("country");
        Iterator<SkillProvider> it = providers.iterator();
        while (it.hasNext()) {
            SkillProvider provider = it.next();

            String cat = provider.getCategory();
            Category category = repositoryService.findCategoryByName(cat);
            if (category == null) {
                category = new Category();
                category.setName(cat);
                repositoryService.save(category);
            }
            String skillName = provider.getSkill();
            Skill skill = repositoryService.findSkillByName(skillName);
            if (skill == null) {
                skill = new Skill();
                skill.setName(skillName);
                skill.setCategory(category);
                repositoryService.save(skill);
            }
            User user = transformToUser(provider, country);
            user.getSkills().add(skill);
            AuthUser existing = repositoryService.findAuthUserByUserName
                    (provider.getProfile().getNationalId());
            if (existing != null) {
                continue;
            }
            for (UserRole role : roles) {
                if (role.getRoleName().equals("Talent")) {
                    user.addUserRole(role);
                    break;
                }
            }
            repositoryService.save(user);
            AuthUser authUser = new AuthUser();
            authUser.setUserName(provider.getProfile().getNationalId());
            authUser.setPassword(UUID.randomUUID().toString());
            authUser.setUser(user);
            repositoryService.save(authUser);
        }
    }

    public User transformToUser(SkillProvider skillProvider, String country) {
        if (skillProvider == null || skillProvider.getProfile() == null) {
            return null;
        }

        TalentProfile profile = skillProvider.getProfile();
        TalentAddress talentAddress = profile.getAddress();

        User user = new User();
        user.setFirstName(profile.getFirstName());
        user.setLastName(profile.getLastName());
        user.setEmail(profile.getEmail());

        City city = repositoryService.fetchCityByName(talentAddress.getCity(), country);
        if(city == null){
            city = new City();
            city.setName(talentAddress.getCity());
            city.setCountry(country);
            repositoryService.save(city);
        }
        Suburb suburb = repositoryService.fetchSuburbByName(city, talentAddress.getSuburb());
        if(suburb == null) {
            suburb = new Suburb();
            suburb.setCity(city);
            suburb.setName(talentAddress.getSuburb());
            repositoryService.save(suburb);
        }

        Address address = new Address();
        address.setSuburb(suburb);
        address.setAddress(String.format("%s %s",
                talentAddress.getStreetNumber(), talentAddress.getStreetName()));
        repositoryService.save(address);
        user.setAddress(address);

        user.setMobilePhone(profile.getPhone());
        user.setDescription(profile.getDescription());
        user.setNationalId(skillProvider.getProfile().getNationalId());
        return user;
    }

    public List<SkillProvider> parseArrayFromFile() throws IOException {
        byte[] bytes = readFileFromClasspath("talent.json");
        Gson gson = new GsonBuilder()
                .create();
        String jsonData = new String(bytes, StandardCharsets.UTF_8);
        Type listType = new com.google.gson.reflect.TypeToken<List<SkillProvider>>() {
        }.getType();
        return gson.fromJson(jsonData, listType);
    }

    private static final String PROFILE = "MUNHU is an eminent professor and a prolific publisher";

    public byte[] readFileFromClasspath(String resourcePath) throws IOException {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return in.readAllBytes();
        }
    }


    private Random random = new Random();

    @Autowired
    private GenericRepository repository;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private Config config;
    private static final Logger LOG = LoggerFactory.getLogger(OrgDataSet.class);
}
