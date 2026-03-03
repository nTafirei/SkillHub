package com.marotech.skillhub.components.dataset;

import com.marotech.skillhub.components.security.FeatureValidator;
import com.marotech.skillhub.components.security.ProtectedElementParser;
import com.marotech.skillhub.components.security.RoleNameParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//DO NOT DELETE!!!
@Component
public class BaseDataSet {

    @Autowired
    private FeatureValidator featureValidator;
    @Autowired
    private RoleNameParser roleNameParser;

    @Autowired
    private ProtectedElementParser protectedElementParser;
}
