package com.marotech.skillhub.action.converters;

import com.marotech.skillhub.model.LanguageModel;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class LanguageModelConverter extends ConverterBase implements TypeConverter<LanguageModel> {


    @Override
    public LanguageModel convert(String input, Class<? extends LanguageModel> targetType,
                                 Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("modelnotfound"));
            return null;
        }

        LanguageModel model;
        try {
            model = repositoryService.findLanguageModelById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("modelnotfound"));
            return null;
        }

        if (model == null) {
            errors.add(new LocalizableError("modelnotfound", input));
            return null;
        }

        return model;
    }

    @Override
    public void setLocale(Locale locale) {
    }

}
