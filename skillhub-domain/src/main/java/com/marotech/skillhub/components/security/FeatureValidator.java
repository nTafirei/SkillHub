package com.marotech.skillhub.components.security;

import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Feature;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class FeatureValidator extends DefaultHandler {

    @Autowired
    private RepositoryService repositoryService;

    private static final Logger log = LoggerFactory
            .getLogger(FeatureValidator.class);

    private String fileName;
    private Map<String, Feature> features = new HashMap<String, Feature>();

    public FeatureValidator() {
        this.fileName = "features.xml";
    }

    public String getFileName() {
        return fileName;
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
        } catch (SAXException e) {
            log.error("SAXException - Error parsing document : " + fileName, e);
            System.exit(0);
        } catch (IOException e) {
            log.error("IOException - Error parsing document : " + fileName, e);
            System.exit(0);
        }
        validateFeatures();
    }

    public void startElement(String nsURI, String strippedName, String tagName,
                             Attributes attributes) throws SAXException {

        if (tagName.equalsIgnoreCase("feature")) {
            String name = attributes.getValue("name");
            Feature feature = new Feature();
            feature.setName(name);
            features.put(name, feature);

        }
    }

    public Feature findFeature(String name) {
        return features.get(name);
    }

    public Map<String, Feature> getFeatures() {
        return features;
    }

    public boolean isValidFeature(String name) {
        return features.containsKey(name);
    }

    private void validateFeatures() {
        for (Feature feature : features.values()) {
            Feature f = repositoryService.findFeatureByName(feature.getName());
            if (f == null) {
                repositoryService.save(feature);
            }
        }
    }
}
