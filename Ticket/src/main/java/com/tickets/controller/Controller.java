package com.tickets.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tickets.controller.dto.response.EventResponse;
import com.tickets.service.GeneralService;

@RestController
@RequestMapping("api/events")
public class Controller {

    private GeneralService service;

    public Controller(GeneralService service) {
        this.service = service;
    }

    @PostMapping("/event/{eventId}/buy-ticket")
    public ResponseEntity<Void> buyTicket(@PathVariable("eventId") String eventId) {
        
        return ResponseEntity.ok().body(null);
    }

    @GetMapping()
    public ResponseEntity<List<EventResponse>> getAll() {
        List<EventResponse> events = service.getAll().stream()
            .map(EventResponse::toResponse)
            .toList();
        return ResponseEntity.ok().body(events);
    }

}
