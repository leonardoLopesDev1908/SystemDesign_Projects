package com.tickets.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.tickets.controller.dto.request.BuyTicketRequest;
import com.tickets.entities.Event;
import com.tickets.entities.Ticket;
import com.tickets.entities.User;
import com.tickets.repository.EventRepository;
import com.tickets.repository.TicketRepository;
import com.tickets.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TicketPurchaseConsumer {

    private final RedisTemplate<String, String> redisTemplate;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketPurchaseConsumer(RedisTemplate<String, String> redisTemplate,
                                  TicketRepository ticketRepository,
                                  UserRepository userRepository,
                                  EventRepository eventRepository
                                ) {
        this.redisTemplate = redisTemplate;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @RabbitListener(queues = "ticket-purchase-queue")
    public void processPurchase(BuyTicketRequest dto) {
        String lockKey = "lock:ticket:" + dto.eventId();
        String lockValue = UUID.randomUUID().toString();
        
        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockValue, Duration.ofSeconds(5));

        if (Boolean.TRUE.equals(acquired)) {
            try {
                Event event = eventRepository.findById(dto.eventId()).orElseThrow(() -> {
                    throw new EntityNotFoundException("Evento não encontrado.");
                });
                
                User user = userRepository.findById(dto.userId())
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                long ticketsVendidos = ticketRepository.countByEventId(event.getId());

                if (ticketsVendidos >= event.getCapacity()) {
                    System.out.println("Ingressos esgotados para o evento: " + event.getName());
                    return;
                }

                Ticket newTicket = new Ticket();
                newTicket.setEvent(event);
                newTicket.setUser(user); 
                
                ticketRepository.save(newTicket);

                System.out.println("Sucesso! Ingresso gerado e comprado para: " + user.getName());

            } finally {
                String currentValue = redisTemplate.opsForValue().get(lockKey);
                if (lockValue.equals(currentValue)) {
                    redisTemplate.delete(lockKey);
                }
            }
        } else {
            System.out.println("Alta concorrência no evento. Reenfileirando requisição...");
        }
    }
}