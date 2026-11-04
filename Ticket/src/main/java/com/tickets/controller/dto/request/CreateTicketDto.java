package com.tickets.controller.dto.request;

public record CreateTicketDto(
    String name,
    String dateTime,
    String category)
{}
