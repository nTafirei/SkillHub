package com.marotech.skillhub.components.processor;

import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Notification;
import com.marotech.skillhub.util.EmailStatus;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailProcessor implements Processor {

    @Autowired
    private RepositoryService repositoryService;
    @Override
    public void process(Exchange exchange) throws Exception {
        Notification notification = exchange.getIn().getBody(Notification.class);
        notification.setEmailStatus(EmailStatus.SENT);
        repositoryService.save(notification);
    }
}
