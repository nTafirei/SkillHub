package com.marotech.skillhub.action.user.converters;

import com.marotech.skillhub.model.Attachment;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class AttachmentConverter extends ConverterBase implements TypeConverter<Attachment> {

    @Override
    public Attachment convert(String input, Class<? extends Attachment> targetType,
                          Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("attachnotfound"));
            return null;
        }

        Attachment attachment;
        try {
            attachment = repositoryService.findAttachmentById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("attachnotfound"));
            return null;
        }

        if (attachment == null) {
            errors.add(new LocalizableError("attachnotfound", input));
            return null;
        }

        return attachment;
    }

    @Override
    public void setLocale(Locale locale) {
    }

}
