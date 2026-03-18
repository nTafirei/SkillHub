package com.marotech.skillhub.api;

import lombok.Data;

@Data
public class JobDTO extends BaseRequest {
    private String name;
    private String deviceLocation;
    private String id;
    private String mimeType;
    private String imageData;
}
