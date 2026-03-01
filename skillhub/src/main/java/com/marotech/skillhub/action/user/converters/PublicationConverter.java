package com.marotech.skillhub.action.user.converters;

import com.marotech.skillhub.model.Publication;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class PublicationConverter extends ConverterBase implements TypeConverter<Publication> {

    @Override
    public Publication convert(String input, Class<? extends Publication> targetType,
                               Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("publicationnotfound"));
            return null;
        }

        Publication publication;
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
