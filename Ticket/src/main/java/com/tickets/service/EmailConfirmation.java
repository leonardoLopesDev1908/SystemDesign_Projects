package com.tickets.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EmailConfirmation {

    @RabbitListener(queues = "email-sender")
    public String sendEmail() {
        return "aa";
    }

}
