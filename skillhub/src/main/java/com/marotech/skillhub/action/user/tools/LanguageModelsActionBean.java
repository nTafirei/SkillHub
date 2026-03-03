package com.marotech.skillhub.action.user.tools;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.LanguageModel;
import lombok.Getter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.util.ArrayList;
import java.util.List;


@UrlBinding("/web/llms")
@RequiresOneRoleOf({"Administrator", "System Administrator", "User"})
public class LanguageModelsActionBean extends UserBaseActionBean {

    @Getter
    private List<LanguageModel> models = new ArrayList<>();

    @DefaultHandler
    public Resolution list() {
        models = repositoryService.findAllLanguageModels();
        return new ForwardResolution(LIST_JSP);
    }

    public int getModelsSize() {
        return models.size();
    }

    @Override
    protected String getErrorPage() {
        return LIST_JSP;
    }

    @Override
    public String getNavSection() {
        return "tools";
    }

    @SpringBean
    private RepositoryService repositoryService;
    private static final String LIST_JSP = "/WEB-INF/jsp/user/tools/llms.jsp";
}
