package com.marotech.skillhub.model;


public enum PubType {

    BOOK("Book"), JOURNAL_ARTICLE("Journal Article"), REVIEW_ARTICLE("Review Article");

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
