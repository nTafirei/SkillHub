package com.marotech.skillhub.action.converters;

import com.marotech.skillhub.model.Notification;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class NotificationConverter extends ConverterBase implements TypeConverter<Notification> {


    @Override
    public Notification convert(String input, Class<? extends Notification> targetType,
                                Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("notificationnotfound"));
            return null;
        }

        Notification notification;
        try {
            notification = repositoryService.findNotificationById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("notificationnotfound"));
            return null;
        }

        if (notification == null) {
            errors.add(new LocalizableError("notificationnotfound", input));
            return null;
        }

        return notification;
    }

    @Override
    public void setLocale(Locale locale) {
    }

}
