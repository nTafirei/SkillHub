package com.marotech.skillhub.action.register;

public enum RegType {

    INTERNAL_USER("A Customer"), TALENT("Talent");

    private String name;

    RegType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
