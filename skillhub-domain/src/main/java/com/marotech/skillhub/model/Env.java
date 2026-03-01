package com.marotech.skillhub.model;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;

public enum Env {
    DEV, TEST, STAGING, PROD, NOT_KNOWN;

    public static Env fromEnv() {
        String env = System.getProperty("env");
        if (StringUtils.isBlank(env)) {
            env = System.getenv("env");
        }
        if (StringUtils.isBlank(env)) {
            return NOT_KNOWN;
        }
        return Env.valueOf(env.toUpperCase(Locale.ROOT));
    }
}
