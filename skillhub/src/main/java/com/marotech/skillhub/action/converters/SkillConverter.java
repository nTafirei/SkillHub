package com.marotech.skillhub.action.converters;

import com.marotech.skillhub.model.Skill;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class SkillConverter extends ConverterBase implements TypeConverter<Skill> {

    @Override
    public Skill convert(String input, Class<? extends Skill> targetType,
                        Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("skillnotfound"));
            return null;
        }

        Skill skill = null;
        try {
            skill = repositoryService.findSkillById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("skillnotfound"));
            return null;
        }

        if (skill == null) {
            errors.add(new LocalizableError("skillnotfound", input));
            return null;
        }

        return skill;
    }

    @Override
    public void setLocale(Locale locale) {
    }

}
