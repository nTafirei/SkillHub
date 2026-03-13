package com.marotech.skillhub.action.datasources;

import com.marotech.skillhub.action.RequiresOneRoleOf;
import com.marotech.skillhub.action.UserBaseActionBean;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.JDBCDataSource;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

@UrlBinding("/web/new-datasource")
@RequiresOneRoleOf({"Administrator", "System Administrator", "User"})
public class NewDataSourceActionBean extends UserBaseActionBean {

    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String name;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String jdbcUrl;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String username;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String password;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String driverClassName;

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(D_LIST_JSP);
    }

    @HandlesEvent(SAVE)
    public Resolution save() {
        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setName(name);
        dataSource.setPassword(password);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setDriverClassName(driverClassName);
        if (testDataSource(dataSource)) {
            repositoryService.save(dataSource);
            return new RedirectResolution(D_LIST);
        }

        getContext().getValidationErrors().add("name",
                new LocalizableError("couldnotvalidatedatasource"));
        return new ForwardResolution(D_LIST_JSP);
    }

    private boolean testDataSource(JDBCDataSource dataSource) {
        try {
            Class.forName(dataSource.getDriverClassName());
            Connection connection = DriverManager.getConnection(dataSource.getJdbcUrl(), dataSource.getUsername(),
                    dataSource.getPassword());
            if (connection.isValid(1)) {
                return true;
            }
        } catch (Exception e) {
            LOG.error("Error testing new datasource{}", dataSource.getName(), e);
        }
        return false;
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    protected String getErrorPage() {
        return D_LIST_JSP;
    }

    @Override
    public String getNavSection() {
        return "datasources";
    }

    @SpringBean
    private RepositoryService repositoryService;
    public static final String D_LIST = "/web/datasources";
    private static final String D_LIST_JSP = "/WEB-INF/jsp/user/datasources/new.jsp";
    private static final Logger LOG = LoggerFactory.getLogger(NewDataSourceActionBean.class);
}
