package com.marotech.skillhub.action.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JSONConverter {

    public <T> T convert(String input, Class<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(input, type);
        } catch (IOException e) {
            LOG.error("Error during conversion to type: " + type + " with input: " + input, e);
            return null;
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(JSONConverter.class);
}

