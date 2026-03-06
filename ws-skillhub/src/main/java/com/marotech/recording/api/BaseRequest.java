package com.marotech.recording.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseRequest {

    protected String token;
    protected String password;
    protected String mobileNumber;

    @Override
    public String toString() {
        return "BaseRequest{" +
                "token='" + token + '\'' +
                ", password='" + password + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}