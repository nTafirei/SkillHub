package com.marotech.skillhub.action.user.datasources;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.model.JDBCDataSource;
import com.marotech.skillhub.components.service.RepositoryService;
import lombok.Getter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.ArrayList;
import java.util.List;


@UrlBinding("/web/datasources")
@RequiresOneRoleOf({"Administrator", "System Administrator", "User"})
public class DataSourcesActionBean extends UserBaseActionBean {

    @Getter
    private List<JDBCDataSource> dataSources = new ArrayList<>();

    @DefaultHandler
    public Resolution list() {
        dataSources = repositoryService.findDataSourcesByOrg(getCurrentUser().getOrg());
        return new ForwardResolution(LIST_JSP);
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    public int getDatasourcesSize() {
        return dataSources.size();
    }

    @Override
    protected String getErrorPage() {
        return LIST_JSP;
    }

    @Override
    public String getNavSection() {
        return "datasources";
    }

    @SpringBean
    private RepositoryService repositoryService;
    private static final String LIST_JSP = "/WEB-INF/jsp/user/datasources/list.jsp";
}
