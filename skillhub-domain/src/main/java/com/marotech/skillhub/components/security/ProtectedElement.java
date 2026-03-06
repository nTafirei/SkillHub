package com.marotech.skillhub.components.security;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProtectedElement {

    private String name;
    private boolean allowGuest;
    private List<String> roles;
    private Set<String> roleSet = new HashSet<String>();

    public ProtectedElement() {
        roles = new ArrayList<>();
        roleSet = new HashSet<String>();
    }

    public void addRole(String roleName) {
        roles.add(roleName);
        roleSet.add(roleName);
    }

    public ProtectedElement(String name, List<String> roles) {
        super();
        this.name = name;
        this.roles = roles;
        roleSet.addAll(roles);
    }

    public boolean hasRole(String roleName) {
        return roleSet.contains(roleName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Set<String> getRoleSet() {
        return roleSet;
    }

    public boolean isAllowGuest() {
        return allowGuest;
    }

    public void setAllowGuest(boolean allowGuest) {
        this.allowGuest = allowGuest;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(name).append(roles).toString();
    }

    public String getXml() {
        StringBuilder builder = new StringBuilder();
        builder.append("&lt;protected-element name=").append(name).append(
                "&gt;");
        for (String role : this.roles) {
            builder.append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&lt;role name=")
                    .append(role).append("/&gt;");
        }
        builder.append("<br/>&lt;/protected-element&gt;");
        return builder.toString();
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
        roleSet = new HashSet<String>();
        roleSet.addAll(roles);
    }

    public boolean hasRoles() {
        return roles.size() > 0;
    }



}
