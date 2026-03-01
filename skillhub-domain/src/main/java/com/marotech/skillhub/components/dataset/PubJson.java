package com.marotech.skillhub.components.dataset;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PubJson {
    @SerializedName("title")
    private String title;

    @SerializedName("source")
    private String source;

    @SerializedName("category")
    private String category;

    @SerializedName("publication_date")
    private String publicationDate;

    @SerializedName("publication_type")
    private String publicationType;

    @SerializedName("workers")
    private Worker[] workers;

    @SerializedName("citations")
    private Citation[] citations;

    @SerializedName("summary")
    private String summary;

    @ToString
    @NoArgsConstructor
    public static class Worker {
        @Getter
        @Setter
        @SerializedName("worker")
        private String worker;
    }
    @ToString
    @NoArgsConstructor
    public static class Citation {
        @Getter
        @Setter
        @SerializedName("citation")
        private String citation;
    }
    public static final String PATTERN = "MMMM d, yyyy";
}
