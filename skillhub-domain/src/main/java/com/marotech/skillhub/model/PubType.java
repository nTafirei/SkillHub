package com.marotech.skillhub.model;


public enum PubType {

    WORK_RELATED_ARTICLE("Work Related Article"),
    SELF_REVIEW_ARTICLE("Self Review Article"),
    THIRD_PARTY_REVIEW_ARTICLE("Third Party Review Article");

    private String type;

    PubType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static PubType fromString(String text) {
        for (PubType c : PubType.values()) {
            if (c.type.equalsIgnoreCase(text)) {
                return c;
            }
        }
        throw new IllegalArgumentException("No enum constant with text " + text);
    }
}
