package com.marotech.recording.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marotech.recording.gson.GsonExcludeField;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class PasswordVerificationRequest {
    protected String password;
    protected String mobileNumber;
    @GsonExcludeField
    @JsonIgnore
    public boolean isValid() {
        return  StringUtils.isNotBlank(mobileNumber)
                && StringUtils.isNotBlank(password);
    }
}