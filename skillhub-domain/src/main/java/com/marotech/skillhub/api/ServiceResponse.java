package com.marotech.skillhub.api;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class ServiceResponse {

    private String message = "OK";
    private int code = HttpCode.OK;

    private String token;

    private ResponseType responseType;

    private Map<String, Object> additionalInfo = new HashMap<>();
}