package com.marotech.skillhub.action.converters;

import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;

public class LocalDateConverter extends ConverterBase implements TypeConverter<LocalDate> {
    @Override
    public LocalDate convert(String input, Class<? extends LocalDate> targetType,
                             Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("datenotfound"));
            return null;
        }

        LocalDate localDateTime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
            localDateTime = LocalDate.parse(input, formatter);
        } catch (Exception e) {
            errors.add(new LocalizableError("datenotfound"));
            return null;
        }

        if (localDateTime == null) {
            errors.add(new LocalizableError("datenotfound", input));
            return null;
        }

        return localDateTime;
    }

    @Override
    public void setLocale(Locale locale) {
    }

    public static final String PATTERN = "dd-MM-yyyy";
}
