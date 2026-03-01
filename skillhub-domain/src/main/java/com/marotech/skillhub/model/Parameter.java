package com.marotech.skillhub.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class Parameter {
    private String value;
    private boolean like;
    private String type;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            LOG.error("Error", e);
            return "{}"; // Fallback to empty JSON
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(Parameter.class);
}
