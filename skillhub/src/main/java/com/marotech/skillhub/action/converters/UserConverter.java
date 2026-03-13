package com.marotech.skillhub.action.converters;

import com.marotech.skillhub.model.User;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class UserConverter extends ConverterBase implements TypeConverter<User> {

    @Override
    public User convert(String input, Class<? extends User> targetType,
                        Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("usernotfound"));
            return null;
        }

        User user = null;
        try {
            user = repositoryService.findUserById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("usernotfound"));
            return null;
        }

        if (user == null) {
            errors.add(new LocalizableError("usernotfound", input));
            return null;
        }

        return user;
    }

    @Override
    public void setLocale(Locale locale) {
    }

}
