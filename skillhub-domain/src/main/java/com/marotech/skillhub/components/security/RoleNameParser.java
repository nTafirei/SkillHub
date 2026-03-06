package com.marotech.skillhub.components.security;

import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.UserRole;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class RoleNameParser extends DefaultHandler {

    private static final Logger log = LoggerFactory
            .getLogger(RoleNameParser.class);

    private String fileName;
    private Map<String, UserRole> roles = new HashMap<String, UserRole>();

    @Autowired
    private RepositoryService repositoryService;

    public RoleNameParser() {
        this.fileName = "roles.xml";
    }

    @PostConstruct
    public void parse() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser sp = spf.newSAXParser();

            InputStream stream = this.getClass().getClassLoader()
                    .getResourceAsStream(fileName);

            sp.parse(stream, this);

        } catch (ParserConfigurationException e) {
            log
                    .error("ParserConfigurationException - Error parsing document : " + fileName
                            , e);
            System.exit(0);
        } catch (Exception e) {
            log.error("IOException - Error parsing document : " + fileName, e);
            System.exit(0);
        }
        validateRoles();
    }

    public void startElement(String nsURI, String strippedName, String tagName,
                             Attributes attributes) throws SAXException {

        if (tagName.equalsIgnoreCase("role")) {
            String role = attributes.getValue("name");
            UserRole userRole = new UserRole();
            userRole.setRoleName(role.trim());
            roles.put(role, userRole);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public Map<String, UserRole> getRoles() {
        return roles;
    }

    private void validateRoles() {
        for (UserRole userRole : roles.values()) {
            UserRole r = repositoryService.findUserRoleByRoleName(userRole.getRoleName());
            if (r == null) {
                repositoryService.save(userRole);
            }
        }
    }
}
