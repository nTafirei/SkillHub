package com.marotech.skillhub.components.dataset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.marotech.skillhub.components.config.Config;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.AuthUser;
import com.marotech.skillhub.model.User;
import com.marotech.skillhub.model.UserRole;
import com.marotech.skillhub.model.Verified;
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
                superAdmin.setAddress("80 Samora Machel Avenue");
                superAdmin.setCity("Harare");
                superAdmin.setCountry("Zimbabwe");
                superAdmin.setMobilePhone("0712374658");
                repository.save(superAdmin);
                superAdmin.setCountry(country);

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
            User user = transformToUser(provider, country);
            AuthUser existing = repositoryService.findAuthUserByUserName
                    (provider.getProfile().getNationalId());
            if(existing != null){
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
        TalentAddress address = profile.getAddress();

        User user = new User();
        user.setFirstName(profile.getFirstName());
        user.setLastName(profile.getLastName());
        user.setAddress(String.format("%s %s",
                address.getStreetNumber(), address.getStreetName()));
        user.setEmail(profile.getEmail());
        user.setCity(address.getCity());
        user.setSuburb(address.getSuburb());
        user.setCountry(country);
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

    private void createUsers() throws Exception {

        if (!isDev) {
            return;
        }

        Iterable<UserRole> roles = repositoryService.findAllRoles();
        String country = config.getProperty("country");
        String ext = config.getProperty(country + ".extension");
        {
            User user0;
            user0 = repositoryService.findUserByEmail("marshall@skillhub.co." + ext);
            if (user0 == null) {
                AuthUser authUser = new AuthUser();
                authUser.setUserName("" + random.nextInt(10020000));
                String newPassword = AuthUser.encodedPassword("test");
                authUser.setPassword(newPassword);
                repository.save(authUser);
                user0 = new User();
                user0.setVerified(Verified.YES);
                user0.setNationalId("" + random.nextInt(1012450000));

                user0.setFirstName("Marshall");
                user0.setCountry(country);
                user0.setLastName("Munhumumwe");
                user0.setNationalId("11123561");
                user0.setEmail("marshall@skillhub.co." + ext);
                user0.setAddress("80 Samora Machel Avenue");
                if (country.equals("Zimbabwe")) {
                    user0.setCity("Harare");
                    user0.setCountry("Zimbabwe");
                } else if (country.equals("Zambia")) {
                    user0.setCity("Chingola");
                    user0.setCountry("Zambia");
                }
                user0.setMobilePhone("" + random.nextInt(30023400));
                repository.save(user0);

                for (UserRole role : roles) {
                    if (role.getRoleName().equals("Human Agent")) {
                        user0.addUserRole(role);
                    }
                }
                repository.save(user0);

                authUser.setUser(user0);
                repository.save(authUser);
            }
        }

        User user1 = repositoryService.findUserByEmail("john@skillhub.co." + ext);
        if (user1 == null) {
            AuthUser authUser = new AuthUser();
            authUser.setUserName("" + random.nextInt(101000));
            String newPassword = AuthUser.encodedPassword("test");
            authUser.setPassword(newPassword);
            repository.save(authUser);
            user1 = new User();
            user1.setVerified(Verified.YES);
            user1.setNationalId("222224222");
            user1.setFirstName("John");
            user1.setLastName("Doe");
            user1.setCountry(country);
            user1.setEmail("john1@skillhub.co." + ext);
            user1.setAddress("80 Samora Machel Avenue");
            if (country.equals("Zimbabwe")) {
                user1.setCity("Harare");
                user1.setCountry("Zimbabwe");
            } else if (country.equals("Zambia")) {
                user1.setCity("Chingola");
                user1.setCountry("Zambia");
            }
            user1.setMobilePhone("" + random.nextInt(10023400));
            repository.save(user1);

            for (UserRole role : roles) {
                if (role.getRoleName().equals("User")) {
                    user1.addUserRole(role);
                    break;
                }
            }

            repository.save(user1);
            authUser.setUser(user1);
            repository.save(authUser);
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
