package com.marotech.skillhub.action.user.converters;

import com.marotech.skillhub.action.user.converters.ConverterBase;
import com.marotech.skillhub.model.City;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class CityConverter extends ConverterBase implements TypeConverter<City> {

    @Override
    public City convert(String input, Class<? extends City> targetType,
                        Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("citynotfound"));
            return null;
        }

        City city = null;
        try {
            city = repositoryService.findCityById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("citynotfound"));
            return null;
        }

        if (city == null) {
            errors.add(new LocalizableError("citynotfound", input));
            return null;
        }

        return city;
    }

    @Override
    public void setLocale(Locale locale) {
    }

}
