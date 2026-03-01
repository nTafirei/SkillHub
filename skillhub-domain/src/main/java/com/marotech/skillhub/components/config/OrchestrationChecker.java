package com.marotech.skillhub.components.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrchestrationChecker {

    @Autowired
    private Config config;
    @Getter
    private boolean enabled;

    @PostConstruct
    public void initEnabledValue() {
        enabled = config.getBooleanProperty("orchestration.enabled");
    }
}
