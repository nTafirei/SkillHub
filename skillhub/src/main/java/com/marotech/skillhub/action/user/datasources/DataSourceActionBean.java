package com.marotech.skillhub.action.user.datasources;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.JDBCDataSourceConverter;
import com.marotech.skillhub.model.JDBCDataSource;
import com.marotech.skillhub.components.service.RepositoryService;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;


@UrlBinding("/web/datasource/{dataSource}/{_eventName}")
@RequiresOneRoleOf({"Administrator", "System Administrator"})
public class DataSourceActionBean extends UserBaseActionBean {

    @DefaultHandler
    public Resolution list() {
        return new ForwardResolution(EDIT_JSP);
    }

    @HandlesEvent(SAVE)
    public Resolution save() {
        dataSource.setName(name);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        repositoryService.save(dataSource);
        return new RedirectResolution(LIST);
    }

    @HandlesEvent(DISABLE)
    public Resolution disable() {
        dataSource.setOnline(Boolean.FALSE);
        repositoryService.save(dataSource);
        return new RedirectResolution(LIST);
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        printNormalizedRequestParameters();
        return new ForwardResolution(getErrorPage());
    }

    @Override
    protected String getErrorPage() {
        return EDIT_JSP;
    }

    @Override
    public String getNavSection() {
        return "datasources";
    }

    @Getter
    @Setter
    @Validate(required = true, converter = JDBCDataSourceConverter.class)
    private JDBCDataSource dataSource;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String name;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String jdbcUrl;
    @Setter
    @Getter
    @Validate(on = SAVE, required = true)
    private String username;
    @Setter
    @Getter
    @Validate(on = SAVE, required = true)
    private String password;
    @Setter
    @Getter
    @Validate(on = SAVE, required = true)
    private String driverClassName;
    @SpringBean
    private RepositoryService repositoryService;

    private static final String LIST = "/web/datasources";
    private static final String EDIT_JSP = "/WEB-INF/jsp/user/datasources/edit.jsp";
}
