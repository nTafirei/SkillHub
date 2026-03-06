package com.marotech.skillhub.action.user.converters;

import com.marotech.skillhub.model.Category;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class CategoryConverter extends ConverterBase implements TypeConverter<Category> {

    @Override
    public Category convert(String input, Class<? extends Category> targetType,
                        Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("categorynotfound"));
            return null;
        }

        Category category = null;
        try {
            category = repositoryService.findCategoryById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("categorynotfound"));
            return null;
        }

        if (category == null) {
            errors.add(new LocalizableError("categorynotfound", input));
            return null;
        }

        return category;
    }

    @Override
    public void setLocale(Locale locale) {
    }

}
