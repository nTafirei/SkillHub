package com.marotech.recording.api;

import lombok.Data;

@Data
public class RecordingDTO extends BaseRequest {
    private String name;
    private String deviceLocation;
    private String id;
    private String mimeType;
    private String imageData;
}
