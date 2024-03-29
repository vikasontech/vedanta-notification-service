/*
 * Copyright (C) 2019  Vikas Kumar Verma
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.vedanta.vidiyalay.email_service.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vedanta.vidiyalay.email_service.EmailVM;
import org.vedanta.vidiyalay.email_service.SendEmailNotification;

@RestController
@RequestMapping("/api")
@Slf4j
public class EmailResources {

    private final SendEmailNotification sendEmailNotification;

    EmailResources(SendEmailNotification sendEmailNotification) {
        this.sendEmailNotification = sendEmailNotification;
    }
    @PostMapping("/mail")
    ResponseEntity sendMail(@RequestBody EmailVM emailVM) {
        Assert.notNull(emailVM, "emailVM is null!");
        log.trace("sending email for {}", emailVM);
        sendEmailNotification.sendEmail(emailVM);
        return ResponseEntity.ok().build();
    }
}
