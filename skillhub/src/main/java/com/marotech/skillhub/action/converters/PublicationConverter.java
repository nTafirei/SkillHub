package com.marotech.skillhub.action.converters;

import com.marotech.skillhub.model.Article;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class PublicationConverter extends ConverterBase implements TypeConverter<Article> {

    @Override
    public Article convert(String input, Class<? extends Article> targetType,
                           Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("publicationnotfound"));
            return null;
        }

        Article publication;
        try {
            publication = repositoryService.findPublicationById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("publicationnotfound"));
            return null;
        }

        if (publication == null) {
            errors.add(new LocalizableError("publicationnotfound", input));
            return null;
        }

        return publication;
    }

    @Override
    public void setLocale(Locale locale) {
    }

}
