package com.marotech.skillhub.action.user;

import com.marotech.skillhub.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FunctionUtil {

    public static boolean isAnalysisOptionSelected(String option, List<String> options) {
        if (option == null || options.size() == 0) {
            return false;
        }
        for (String t : options) {
            if (t.equals(option)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSelected(User user, Set<User> users) {
        if (user == null || users.isEmpty()) {
            return false;
        }
        return users.contains(user);
    }

    public static boolean isMany(Collection<Object> objects) {
        if (objects == null || objects.isEmpty()) {
            return false;
        }
        return objects.size() > 1;
    }

    public static boolean hasMore(Integer index, Collection<Object> objects) {
        if (objects == null || objects.isEmpty()) {
            return false;
        }
        return index < (objects.size() - 1);
    }

    public static Boolean hasFailedValidation(String field,
                                              net.sourceforge.stripes.validation.ValidationErrors errors) {
        for (String key : errors.keySet()) {
            if (key.equals(field)) {
                return true;
            }
        }
        return false;
    }
}
