package org.vedanta.vidiyalay.email_service.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ObjectUtils;
import org.vedanta.vidiyalay.email_service.EmailVM;
import org.vedanta.vidiyalay.email_service.SendEmailNotification;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
@Ignore
public class SendEmailNotificationImplTest {
//    @Autowired
    SendEmailNotification sendEmailNotification;
//    @Test
    public void sendEmail() {

        final EmailVM emailVM = EmailVM.builder()
                .to("vikas.on@gmail.com")
                .subject("test mail")
                .text("this is message").build();

        sendEmailNotification.sendEmail(emailVM);
    }
}
