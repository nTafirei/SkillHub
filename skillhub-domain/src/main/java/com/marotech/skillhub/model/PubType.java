package com.marotech.skillhub.model;


public enum PubType {

    WORK_RELATED("Work Related"),
    SELF_REVIEW("Self Review"),
    THIRD_PARTY_REVIEW("Third Party Review");

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
