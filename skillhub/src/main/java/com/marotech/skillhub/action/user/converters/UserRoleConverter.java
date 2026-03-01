package com.marotech.skillhub.action.user.converters;

import com.marotech.skillhub.model.UserRole;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class UserRoleConverter extends ConverterBase implements TypeConverter<UserRole> {

    @Override
    public UserRole convert(String input, Class<? extends UserRole> targetType,
                            Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("userrolenotfound"));
            return null;
        }

        UserRole role;
        try {
            role = repositoryService.findUserRoleById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("userrolenotfound"));
            return null;
        }

        if (role == null) {
            errors.add(new LocalizableError("userrolenotfound", input));
            return null;
        }

        return role;
    }

    @Override
    public void setLocale(Locale locale) {
    }

}
