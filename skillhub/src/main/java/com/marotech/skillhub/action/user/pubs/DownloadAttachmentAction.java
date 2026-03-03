package com.marotech.skillhub.action.user.pubs;

import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.AttachmentConverter;
import com.marotech.skillhub.model.Attachment;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

import java.io.ByteArrayInputStream;

@UrlBinding("/web/download-attachment")
public class DownloadAttachmentAction extends UserBaseActionBean {

	@Getter
	@Setter
	@Validate(required = true, converter = AttachmentConverter.class)
	private Attachment attachment;

	@DefaultHandler
	public Resolution downloadAttachment() {
		return new StreamingResolution(attachment.getContentType(),
				new ByteArrayInputStream(attachment.getData()));
					//.setFilename(attachment.getName());
	}

	@Override
	public String getNavSection() {
		return "publications";
	}
}
