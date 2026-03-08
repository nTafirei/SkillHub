package com.marotech.skillhub.components.processor;

import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Notification;
import com.marotech.skillhub.util.MessageStatus;
import com.marotech.skillhub.util.SMSStatus;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

public class SMSProcessor implements Processor {

    @Autowired
    private RepositoryService repositoryService;
    @Override
    public void process(Exchange exchange) throws Exception {
        Notification notification = exchange.getIn().getBody(Notification.class);
        notification.setSmsStatus(SMSStatus.SENT);
        repositoryService.save(notification);
    }
}
