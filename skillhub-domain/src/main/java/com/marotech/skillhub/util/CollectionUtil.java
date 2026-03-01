package com.marotech.skillhub.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtil {

    public static boolean isNotEmpty(List<?> col) {
        return !isEmpty(col);
    }

    public static boolean isNotEmpty(Collection<?> col) {
        return !isEmpty(col);
    }

    public static boolean isNotEmpty(Set<?> col) {
        return !isEmpty(col);
    }

    public static boolean isNotEmpty(Map<?, ?> col) {
        return !isEmpty(col);
    }

    public static boolean isEmpty(List<?> col) {
        return col == null || col.isEmpty();
    }

    public static boolean isEmpty(Collection<?> col) {
        return col == null || col.isEmpty();
    }

    public static boolean isEmpty(Set<?> col) {
        return col == null || col.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> col) {
        return col == null || col.isEmpty();
    }

}
