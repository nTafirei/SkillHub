package com.marotech.skillhub.action.converters;

import com.marotech.skillhub.components.service.RepositoryService;
import net.sourceforge.stripes.integration.spring.SpringBean;

public class ConverterBase {
    @SpringBean
    protected RepositoryService repositoryService;
}
