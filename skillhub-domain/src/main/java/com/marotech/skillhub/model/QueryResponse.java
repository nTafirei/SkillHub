package com.marotech.skillhub.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class QueryResponse {
    private SQLQuery query;
    private String response;

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

    private static final Logger LOG = LoggerFactory.getLogger(QueryResponse.class);
}
