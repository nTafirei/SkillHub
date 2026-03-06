package com.marotech.recording.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marotech.recording.gson.GsonExcludeField;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class OTPVerificationRequest{
    private String otp;
    protected String mobileNumber;
    @GsonExcludeField
    @JsonIgnore
    public boolean isValid() {
        if (StringUtils.isNotBlank(mobileNumber) && StringUtils.isNotBlank(otp)) {
            return true;
        }
        return  false;
    }
}