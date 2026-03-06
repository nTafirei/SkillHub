package com.marotech.recording.ws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.marotech.recording.config.Config;
import com.marotech.recording.gson.CustomExclusionStrategy;
import com.marotech.recording.gson.LocalDateTimeAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public abstract class BaseController {

    protected static final Gson GSON = new GsonBuilder()
            .setExclusionStrategies(new CustomExclusionStrategy())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Autowired
    protected Config config;

    protected boolean shouldAudit() {
        return config.getBooleanProperty("app.should.audit");
    }
    public static final String TOKEN_NOT_FOUND = "Specified token was not found in our system. Please try again or contact our customer service team";
    public static final String TTL = "ttl";
    public static final String NULL_REQUEST_FOUND = "Null request found";
    public static final String RECORDINGS = "recordings";
    public static final String SEC_QUESTION_1 = "securityQuestion1";
    public static final String SEC_QUESTION_2 = "securityQuestion2";
    public static final String SEC_QUESTION_3 = "securityQuestion3";
    public static final String APP_SESSION_TTL = "app.session.ttl";
    public static final String REG_RESPONSE = "regResponse";
    public static final String RESPONSE_TYPE = "responseType";

    public static final String NO_VOUCHERS_FOUND = "No vouchers found";
    protected String getPurchaseCurrency() {
        return config.getProperty("app.purchase.currency");
    }
}
