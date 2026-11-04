package com.tickets.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tickets")
public class Controller {

    @PostMapping("/ticket/{id}/buy/")
    public ResponseEntity<Void> buyTicket(@PathVariable("id") String ticketId) {
        return ResponseEntity.ok().body(null);
    }
}
