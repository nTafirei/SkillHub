package com.marotech.skillhub.action.user;

import com.marotech.skillhub.model.Frequency;
import jakarta.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class UserBaseActionBean extends BaseActionBean {

    protected boolean isPresent(Object o) {
        return o != null;
    }

    @Override
    protected Resolution getHomePage() {
        return new RedirectResolution(WEB_USER_HOME);
    }

    public Boolean getIsAdvanced() {
        String advanced = config.getProperty("app.platform");
        if ("advanced".equalsIgnoreCase(advanced)) {
            return true;
        }
        return false;
    }

    protected Resolution streamingResolution(int status, String data) {
        return new StreamingResolution("application/txt") {
            public void stream(final HttpServletResponse response) {
                try {
                    final OutputStream out = response.getOutputStream();
                    out.write(data.getBytes());
                    response.setStatus(status);
                    response.setHeader("Access-Control-Allow-Origin", "*");
                } catch (Exception e) {
                    throw new IllegalStateException(e.getMessage());
                }
            }
        };
    }

    public List<Frequency> getFrequencies() {
        List<Frequency> frequencies = new ArrayList<>();
        if (getIsAdvanced()) {
            for (Frequency frequency : Frequency.values()) {
                frequencies.add(frequency);
            }
        } else {
            frequencies.add(Frequency.REPEATING);
            frequencies.add(Frequency.NON_REPEATING);
        }
        return frequencies;
    }

    private static final String WEB_USER_HOME = "/web/home";
}
