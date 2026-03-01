package com.marotech.skillhub.model;

public enum Gender {

    MALE("Male"), FEMALE("Female");

    private String name;

    private Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getLinkName() {
        return name.toLowerCase();
    }
}
