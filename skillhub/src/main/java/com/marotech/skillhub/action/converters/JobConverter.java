package com.marotech.skillhub.action.converters;

import com.marotech.skillhub.model.Job;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class JobConverter extends ConverterBase implements TypeConverter<Job> {

    @Override
    public Job convert(String input, Class<? extends Job> targetType,
                        Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("jobnotfound"));
            return null;
        }

        Job job = null;
        try {
            job = repositoryService.findJobById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("jobnotfound"));
            return null;
        }

        if (job == null) {
            errors.add(new LocalizableError("jobnotfound", input));
            return null;
        }

        return job;
    }

    @Override
    public void setLocale(Locale locale) {
    }

}
