package com.tickets.Utils;

import com.tickets.config.RabbitQueue;
import com.tickets.Utils.payload.EmailPayload;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmailProducer {

    private static final String EMAIL_TO = "leonardosilvalls1908@gmail.com";
    private static final String EMAIL_SUBJECT = "Purchase Ticket Confirmation";

    private final RabbitTemplate rabbitTemplate;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void queueEmail(String text){
        rabbitTemplate.convertAndSend(
                RabbitQueue.EXCHANGE_NAME,
                RabbitQueue.ROUTING_KEY,
                new EmailPayload(EMAIL_TO, EMAIL_SUBJECT, text)
        );
    }
}
