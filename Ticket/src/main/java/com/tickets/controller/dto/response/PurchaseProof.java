package com.tickets.controller.dto.response;

import com.tickets.entities.Ticket;

import java.time.LocalDateTime;
import java.util.UUID;

public class PurchaseProof {
    private UUID id;
    private Long ticketId;
    private String timestamp;

    public PurchaseProof(UUID id, Long ticketId, String timestamp){
        this.id = id;
        this.ticketId = ticketId;
        this.timestamp = timestamp;
    }

    public static PurchaseProof fromTicket(Ticket ticket) {
        return new PurchaseProof(
                UUID.randomUUID(),
                ticket.getId(),
                LocalDateTime.now().toString()
        );
    }
}
