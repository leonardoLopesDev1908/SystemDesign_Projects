package com.tickets.controller.dto.request;

import java.util.UUID;

public record BuyTicketRequest(
    UUID userId,
    Long eventId
)
{}
