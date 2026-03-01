package com.marotech.skillhub.action.user.pubs;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.components.service.RepositoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

@WebServlet
@MultipartConfig(fileSizeThreshold = 20971520) // 20MB
@UrlBinding("/web/submit-publication")
@RequiresOneRoleOf({"Administrator", "System Administrator", "User"})
public class UploadPubActionBean extends UserBaseActionBean {

    @Setter
    private FileBean fileBean;
    @Getter
    private String message;
    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(JSP);
    }

    @HandlesEvent(UPLOAD)
    public Resolution upload() throws ServletException, IOException {

        ForwardResolution resolution = processFile();
        if (resolution != null) {
            message = "Could not process the uploaded file";
            return resolution;
        }
        return new RedirectResolution("/web/publications");
    }
    private ForwardResolution processFile() throws ServletException, IOException {

        InputStream imageStream;
        try {
            imageStream = fileBean.getInputStream();
            byte[] bytes = imageStream.readAllBytes();
            String fileName = fileBean.getFileName();
            boolean valid = false;
            if(fileName.toLowerCase().endsWith(".pdf")
                    || fileName.toLowerCase().endsWith(".txt")){
                valid = true;
            }
            if(!valid){
                getContext().getValidationErrors().add("fileBean",
                        new LocalizableError("invalidformatfound"));
                return new ForwardResolution(JSP);
            }
            String path = config.getProperty("uploads.incoming.file.dir");
            try {
                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(path + File.separator + fileName));
                writer.write(new String(bytes, StandardCharsets.UTF_8));
                writer.close();
            } catch (Exception ex) {
                LOG.error("Error", ex);
            }
            message = "File has been accepted for processing";
        } catch (Exception e) {
            LOG.error("Error", e);
            getContext().getValidationErrors().add("fileBean",
                    new LocalizableError("filenotfound"));
        }

        if (getContext().getValidationErrors().keySet().size() > 0) {
            return new ForwardResolution(JSP);
        }
        return null;
    }
    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    @Override
    protected String getErrorPage() {
        return JSP;
    }

    @Override
    public String getNavSection() {
        return "active-publications";
    }

    @SpringBean
    private RepositoryService repositoryService;

    private static final Logger LOG = LoggerFactory.getLogger(UploadPubActionBean.class);
    public static final String UPLOAD = "upload";
    public static final String JSP = "/WEB-INF/jsp/user/pubs/upload.jsp";
}