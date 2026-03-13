package com.marotech.skillhub.action.converters;

import com.marotech.skillhub.model.Comment;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class CommentConverter extends ConverterBase implements TypeConverter<Comment> {

    @Override
    public Comment convert(String input, Class<? extends Comment> targetType,
                               Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("commentnotfound"));
            return null;
        }

        Comment comment;
        try {
            comment = repositoryService.findCommentById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("commentnotfound"));
            return null;
        }

        if (comment == null) {
            errors.add(new LocalizableError("commentnotfound", input));
            return null;
        }

        return comment;
    }

    @Override
    public void setLocale(Locale locale) {
    }

}
