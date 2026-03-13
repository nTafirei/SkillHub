package com.marotech.skillhub.action.converters;

import com.marotech.skillhub.model.JDBCDataSource;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

public class JDBCDataSourceConverter extends ConverterBase implements TypeConverter<JDBCDataSource> {
    @Override
    public JDBCDataSource convert(String input, Class<? extends JDBCDataSource> targetType,
                                  Collection<ValidationError> errors) {

        if (StringUtils.isBlank(input)) {
            errors.add(new LocalizableError("datasourcenotfound"));
            return null;
        }

        JDBCDataSource dataSource;
        try {
            dataSource = repositoryService.findJDBCDataSourceById(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("datasourcenotfound"));
            return null;
        }
        if (dataSource == null) {
            errors.add(new LocalizableError("datasourcenotfound", input));
            return null;
        }

        return dataSource;
    }

    @Override
    public void setLocale(Locale locale) {
    }

    private static final String PATTERN = "dd-MM-yyyy HH:mm";
}
