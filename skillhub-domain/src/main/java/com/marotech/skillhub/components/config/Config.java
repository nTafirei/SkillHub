package com.marotech.skillhub.components.config;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Properties;

public class Config {
    private Properties props = new Properties();

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    public String getProperty(String property) {
        if (StringUtils.isBlank(property)) {
            return null;
        }
        return props.getProperty(property);
    }

    public boolean getBooleanProperty(String property) {
        if (StringUtils.isBlank(property)) {
            return false;
        }
        return Boolean.valueOf(props.getProperty(property));
    }

    public BigDecimal getBigDecimalProperty(String property) {
        if (StringUtils.isBlank(property)) {
            return null;
        }
        return new BigDecimal(props.getProperty(property));
    }

    public Float getFloatProperty(String property) {
        if (StringUtils.isBlank(property)) {
            return null;
        }
        return Float.valueOf(props.getProperty(property));
    }

    public Double getDoubleProperty(String property) {
        if (StringUtils.isBlank(property)) {
            return null;
        }
        return Double.valueOf(props.getProperty(property));
    }

    public Integer getIntegerProperty(String property) {
        if (StringUtils.isBlank(property)) {
            return null;
        }
        return Integer.valueOf(props.getProperty(property));
    }

    public void setProperty(String key, String value) {
        props.setProperty(key, value);
    }
}
