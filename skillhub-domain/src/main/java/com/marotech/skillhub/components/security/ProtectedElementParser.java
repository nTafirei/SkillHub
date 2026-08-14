package com.marotech.skillhub.components.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProtectedElementParser extends DefaultHandler {

    private static final Logger log = LoggerFactory
            .getLogger(ProtectedElementParser.class);

    private String fileName;
    private ProtectedElement element;
    private List<ProtectedElement> elements = new ArrayList<ProtectedElement>();

    private FeatureValidator featureValidator;
    private RoleNameParser roleNameParser;

    public ProtectedElementParser(FeatureValidator featureValidator,
                                  RoleNameParser roleNameParser) {
        this.fileName = "security-policy.xml";
        this.featureValidator = featureValidator;
        this.roleNameParser = roleNameParser;
        parse();
    }

    public String getFileName() {
        return fileName;
    }

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
            log.error("Exception - Error parsing document : " + fileName, e);
            System.exit(0);
        }
    }

    public void startElement(String nsURI, String strippedName, String tagName,
                             Attributes attributes) throws SAXException {

        if (tagName.equalsIgnoreCase("protected-element")) {
            element = new ProtectedElement();
            String name = attributes.getValue("name");

            if (StringUtils.isBlank(name)) {
                log.error(
                        "Security/Feature error : "
                                + " could not find name of protected-element tag. Please make sure "
                                + fileName + " and "
                                + featureValidator.getFileName()
                                + " match for : " + tagName);
                System.exit(0);
            }

            if (!featureValidator.isValidFeature(name)) {
                log.error(
                        "Security/Feature error : "
                                + name
                                + " is defined as a tag protected-element but is not listed as a valid feature. Please make sure "
                                + fileName + " and "
                                + featureValidator.getFileName()
                                + " match for " + name);
                System.exit(0);
            }
            element.setName(name);
            elements.add(element);

            String enabled = attributes.getValue("enabled");
            if (StringUtils.isNotBlank(enabled)) {
                element.setEnabled(Boolean.valueOf(enabled));
            }

            String allowGuest = attributes.getValue("allowGuest");
            if (StringUtils.isNotBlank(allowGuest)) {
                element.setAllowGuest(Boolean.valueOf(allowGuest));
            }
        } else if (tagName.equalsIgnoreCase("role")) {
            String role = attributes.getValue("name");

            if (!isValidRole(role)) {
                log.error(
                        "Security/Feature error : "
                                + role
                                + " is defined as a role in a protected-element but is not listed as a valid role. Please make sure "
                                + fileName + " and "
                                + roleNameParser.getFileName() + " match for "
                                + role);
                System.exit(0);
            }
            element.addRole(role);
        }
    }

    private boolean isValidRole(String role) {
        return roleNameParser.getRoles().containsKey(role);
    }

    public List<ProtectedElement> getElements() {
        return elements;
    }

}
