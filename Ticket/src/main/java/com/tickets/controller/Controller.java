package com.tickets.controller;

import java.util.List;

import com.tickets.controller.dto.request.BuyTicketRequest;
import com.tickets.controller.dto.response.PurchaseProof;
import com.tickets.service.TicketPurchaseConsumer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tickets.controller.dto.response.EventResponse;
import com.tickets.service.GeneralService;

@RestController
@RequestMapping("api/events")
public class Controller {

    private final GeneralService service;
    private final TicketPurchaseConsumer purchaser;

    public Controller(GeneralService service, TicketPurchaseConsumer purchaser) {
        this.service = service;
        this.purchaser = purchaser;
    }

    @PostMapping("/event/buy-ticket")
    public ResponseEntity<PurchaseProof> buyTicket(@RequestBody BuyTicketRequest buyRequest) {
        PurchaseProof proof = purchaser.processPurchase(buyRequest);
        return ResponseEntity.ok().body(proof);
    }

    @GetMapping()
    public ResponseEntity<List<EventResponse>> getAll() {
        List<EventResponse> events = service.getAll().stream()
            .map(EventResponse::toResponse)
            .toList();
        return ResponseEntity.ok().body(events);
    }
}
