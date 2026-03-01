package com.marotech.skillhub.components.dataset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.marotech.skillhub.components.config.Config;
import com.marotech.skillhub.model.*;
import com.marotech.skillhub.repository.GenericRepository;
import com.marotech.skillhub.gson.CustomExclusionStrategy;
import com.marotech.skillhub.gson.LocalDateTimeAdapter;
import com.marotech.skillhub.components.processor.LocalDateAdapter;
import com.marotech.skillhub.components.service.RepositoryService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
            long num = repositoryService.countOrgs();
            if (num > 0) {
                return;
            }

            Org org = new Org();
            org.setAddress("80 Samora Machel Avenue");
            org.setName("MaroTech");
            org.setEmail("admin@marotech.co");
            repository.save(org);

            User superAdmin = null;

            if (superAdmin == null) {
                AuthUser authUser = new AuthUser();
                authUser.setUserName("system_admin@skillhub.co." + ext);
                String newPassword = AuthUser.encodedPassword("test");
                authUser.setPassword(newPassword);

                repository.save(authUser);
                superAdmin = new User();
                //superAdmin.setDescription("Super Admin");
                superAdmin.setUserType(UserType.HUMAN);
                superAdmin.setVerified(Verified.YES);
                superAdmin.setNationalId("12345001");

                superAdmin.setFirstName("System Admin");
                superAdmin.setLastName("System Admin");
                superAdmin.setMiddleName("System Admin");
                superAdmin.setEmail(adminEmail);
                superAdmin.setAddress("80 Samora Machel Avenue");
                superAdmin.setGender(Gender.MALE);
                superAdmin.setCity("Harare");
                superAdmin.setCountry("Zimbabwe");
                superAdmin.setMobilePhone("0712374658");
                superAdmin.setOrg(org);
                repository.save(superAdmin);

                Iterable<UserRole> roles = repositoryService.findAllRoles();

                for (UserRole role : roles) {
                    if (role.getRoleName().equals("System Administrator")) {
                        superAdmin.addUserRole(role);
                        break;
                    }
                }
                repository.save(superAdmin);
                authUser.setSystemUser(superAdmin);
                repository.save(authUser);

                createWorkers(superAdmin);
            }

            createOrgUsers(org);
        } catch (Exception e) {
            LOG.error("Error building basic data :", e);
            System.exit(0);
        }
    }

    private void createWorkers(User user) throws IOException {
        byte[] bytes = readFileFromClasspath("pubs.json");
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new CustomExclusionStrategy())
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String jsonData = new String(bytes, StandardCharsets.UTF_8);
        Type publicationListType = new com.google.gson.reflect.TypeToken<List<PubJson>>() {
        }.getType();

        List<PubJson> pubs = gson.fromJson(jsonData, publicationListType);
        int index = 0;
        Iterator<PubJson> it0 = pubs.iterator();
        while (it0.hasNext()) {
            PubJson p = it0.next();
            Publication pub = new Publication();
            if (index % 2 == 0) {
                pub.setShowcase(Showcase.YES);
            }
            if (index % 3 == 0) {
                pub.setActiveStatus(ActiveStatus.NOT_ACTIVE);
            }
            index = index + 1;
            pub.setTitle(p.getTitle());
            pub.setSource(p.getSource());
            pub.setSummary(p.getSummary());

            PubJson.Worker[] workerList = p.getWorkers();
            for (PubJson.Worker a : workerList) {
                Worker worker =
                        new Worker();
                String aStr = a.getWorker();
                if (aStr.contains("Dr. ")) {
                    worker.setTitle("Dr.");
                }
                if (index % 2 == 0) {
                    worker.setShowcase(Showcase.YES);
                }
                String name = aStr.replaceAll("Dr. ", "");
                String[] names = name.split(" ");
                worker.setFirstName(names[0]);
                worker.setLastName(names[1]);
                worker.setEmail((worker.getLastName() + "@gmail.com").toLowerCase());
                worker.setProfile(PROFILE.replaceAll("MUNHU", worker.getFullName()));
                List<Worker> tmp = repositoryService.findWorkerByNames(worker.getFirstName(), worker.getLastName());
                if (tmp.isEmpty()) {
                    repositoryService.save(worker);
                } else {
                    worker = tmp.get(0);
                }
                pub.getWorkers().add(worker);
            }

            Category cat = Category.fromString(p.getCategory());
            pub.setCategory(cat);
            PubType pubType = PubType.fromString(p.getPublicationType());
            pub.setPubType(pubType);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PubJson.PATTERN);
            LocalDate localDate = LocalDate.parse(p.getPublicationDate(), formatter);
            pub.setPubDate(localDate);
            for (PubJson.Citation c : p.getCitations()) {
                pub.getCitations().add(c.getCitation());
            }

            Attachment attachment = new Attachment();
            bytes = readFileFromClasspath("sample-pub.pdf");
            attachment.setName(pub.getTitle());
            attachment.setData(bytes);
            pub.setFileName("sample-pub.pdf");
            attachment.setSize(bytes.length);
            attachment.setContentType("application/pdf");
            repositoryService.save(attachment);
            pub.setAttachment(attachment);
            Comment comment = new Comment();
            comment.setCreatedBy(user);
            comment.setTitle("Commenting on : " + pub.getTitle());
            comment.setBody("this is a test comment");
            repositoryService.save(comment);
            pub.getComments().add(comment);
            pub.setOrg(user.getOrg());
            repositoryService.save(pub);
        }
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

    private void createOrgUsers(Org org) throws Exception {

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
                user0.setUserType(UserType.HUMAN);
                user0.setOrg(org);
                user0.setVerified(Verified.YES);
                user0.setNationalId("" + random.nextInt(1012450000));

                user0.setFirstName("Marshall");
                user0.setLastName("Munhumumwe");
                user0.setNationalId("11123561");
                user0.setEmail("marshall@skillhub.co." + ext);
                user0.setAddress("80 Samora Machel Avenue");
                user0.setGender(Gender.MALE);
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

                authUser.setSystemUser(user0);
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
            user1.setUserType(UserType.HUMAN);
            user1.setOrg(org);
            user1.setVerified(Verified.YES);
            user1.setNationalId("222224222");
            user1.setFirstName("John");
            user1.setLastName("Doe");
            user1.setEmail("john1@skillhub.co." + ext);
            user1.setAddress("80 Samora Machel Avenue");
            user1.setGender(Gender.MALE);

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
            authUser.setSystemUser(user1);
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
