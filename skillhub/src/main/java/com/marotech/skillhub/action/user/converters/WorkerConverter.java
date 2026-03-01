package com.marotech.skillhub.action.user.converters;

import com.marotech.skillhub.model.Worker;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class WorkerConverter extends ConverterBase implements TypeConverter<Worker> {

    @Override
    public Worker convert(String input, Class<? extends Worker> targetType,
                          Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("workernotfound"));
            return null;
        }

        Worker worker;
        try {
            worker = repositoryService.findWorkerById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("workernotfound"));
            return null;
        }

        if (worker == null) {
            errors.add(new LocalizableError("workernotfound", input));
            return null;
        }

        return worker;
    }

    @Override
    public void setLocale(Locale locale) {
    }

}
