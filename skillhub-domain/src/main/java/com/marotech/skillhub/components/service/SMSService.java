package com.marotech.skillhub.components.service;

import com.marotech.skillhub.model.Notification;
import com.marotech.skillhub.util.MessageStatus;

public class SMSService {

    public MessageStatus send(Notification notification){
        return MessageStatus.CREATED;
    }
}
