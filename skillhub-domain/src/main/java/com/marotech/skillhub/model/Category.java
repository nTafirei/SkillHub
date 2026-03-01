package com.marotech.skillhub.model;


import com.marotech.skillhub.util.CategoryComparator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Category {
    MATHS("Maths"), ENGLISH("English"), PSYCHOLOGY("Psychology"),
    PHYSICS("Physics"), CHEMISTRY("Chemistry"), BIOLOGY("Biology"),
    COMPUTER_SCIENCE("Computer Science"), HISTORY("History"), ART("Art"),
    POLITICAL_SCIENCE("Political Science"), ACCOUNTING("Accounting"),
    FINANCE("Finance"), BANKING("Banking"), ENVIRONMENTAL_SCIENCE("Environmental Science"),
    AGRICULTURE("Agriculture"), ENVIRONMENT_AND_FOOD_SYSTEMS("Environment and Food Systems"),
    BUSINESS_MANAGEMENT("Business Management "), EDUCATION("Education"), LAW("Law"),
    MEDICINE("Medicine"), BEHAVIORAL_SCIENCES("Behavioural Sciences"),
    VETERINARY_SCIENCE("Veterinary Science"), ENGINEERING("Engineering"),
    HUMANITIES("Humanities");


    private String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<Category> getSortedValues() {
        List<Category> cats = Arrays.asList(values());
        Collections.sort(cats, new CategoryComparator());
        return cats;
    }

    public static Category fromString(String text) {
        for (Category c : Category.values()) {
            if (c.name.equalsIgnoreCase(text)) {
                return c;
            }
        }
        throw new IllegalArgumentException("No enum constant with text " + text);
    }
}
