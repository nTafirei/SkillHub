package com.marotech.skillhub.action.converters;

import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;

public class LocalDateTimeConverter extends ConverterBase implements TypeConverter<LocalDateTime> {
    @Override
    public LocalDateTime convert(String input, Class<? extends LocalDateTime> targetType,
                                 Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("datetimenotfound"));
            return null;
        }

        LocalDateTime localDateTime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
            localDateTime = LocalDateTime.parse(input, formatter);
        } catch (Exception e) {
            errors.add(new LocalizableError("datetimenotfound"));
            return null;
        }

        if (localDateTime == null) {
            errors.add(new LocalizableError("datetimenotfound", input));
            return null;
        }

        return localDateTime;
    }

    @Override
    public void setLocale(Locale locale) {
    }

    private static final String PATTERN = "dd-MMMM-yyyy HH:mm";
}
