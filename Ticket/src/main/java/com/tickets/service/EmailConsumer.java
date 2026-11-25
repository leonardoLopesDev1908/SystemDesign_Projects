package com.tickets.service;

import com.tickets.config.RabbitQueue;
import com.tickets.service.payload.EmailPayload;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    private final JavaMailSender mailSender;

    public EmailConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = RabbitQueue.QUEUE_NAME)
    public void sendPlainText(EmailPayload payload) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(payload.getTo());
        message.setSubject(payload.getSubject());
        message.setText(payload.getBody());

        mailSender.send(message);
    }
}
