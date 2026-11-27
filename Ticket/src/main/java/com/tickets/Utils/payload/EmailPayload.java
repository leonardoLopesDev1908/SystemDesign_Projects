package com.tickets.Utils.payload;

import lombok.Getter;

@Getter
public class EmailPayload {
    private String to;
    private String subject;
    private String body;

    public EmailPayload() {}

    public EmailPayload(String to, String subject, String body){
        this.to = to;
        this.subject = subject;
        this.body = body;
    }
}
