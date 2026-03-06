package com.marotech.skillhub.action.user.converters;

import com.marotech.skillhub.model.Suburb;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class SuburbConverter extends ConverterBase implements TypeConverter<Suburb> {

    @Override
    public Suburb convert(String input, Class<? extends Suburb> targetType,
                        Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("suburbnotfound"));
            return null;
        }

        Suburb cuburb = null;
        try {
            cuburb = repositoryService.findSuburbById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("suburbnotfound"));
            return null;
        }

        if (cuburb == null) {
            errors.add(new LocalizableError("suburbnotfound", input));
            return null;
        }

        return cuburb;
    }

    @Override
    public void setLocale(Locale locale) {
    }

}
