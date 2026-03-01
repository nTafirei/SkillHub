package com.marotech.skillhub.util;

import com.marotech.skillhub.model.Category;

import java.util.Comparator;


public class CategoryComparator implements Comparator<Category> {
    @Override
    public int compare(Category task1, Category task2) {
        if (task1 == null && task2 == null) {
            return 0; // Both are null, consider them equal
        } else if (task1 == null) {
            return -1; // task1 is null, consider it less than task2
        } else if (task2 == null) {
            return 1; // task2 is null, consider it less than task1
        } else {
            return task1.getName().compareTo(task2.getName());
        }
    }
}