package com.marotech.skillhub.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString(callSuper = true)
public class JobMetadata {

    private String jobType;
    private String id;
    private String name;
    private String token;
    private String agentType;

    public boolean validate() {
        if (StringUtils.isAnyBlank(jobType, name, id, agentType, token)) {
            return false;
        }
        return true;
    }
}
