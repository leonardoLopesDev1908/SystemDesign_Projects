package com.tickets.service;

import java.time.Duration;
import java.util.UUID;

import com.tickets.Utils.RedisDistributedLock;
import com.tickets.controller.dto.response.PurchaseProof;
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

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    private final RedisDistributedLock redisLock;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketPurchaseConsumer(RedisDistributedLock redisLock,
                                  TicketRepository ticketRepository,
                                  UserRepository userRepository,
                                  EventRepository eventRepository
                                ) {
        this.redisLock = redisLock;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @RabbitListener(queues = "ticket-purchase-queue")
    public PurchaseProof processPurchase(BuyTicketRequest dto) {
        String lockKey = "lock:ticket:" + dto.eventId();
        String lockValue = redisLock.acquiredLock(lockKey, DEFAULT_TIMEOUT);

        if (lockValue != null) {
            try {
                Event event = eventRepository.findById(dto.eventId()).orElseThrow(() -> {
                    throw new EntityNotFoundException("Evento não encontrado.");
                });

                User user = userRepository.findById(dto.userId())
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                long ticketsVendidos = ticketRepository.countByEventId(event.getId());

                if (ticketsVendidos >= event.getCapacity()) {
                    System.out.println("Ingressos esgotados para o evento: " + event.getName());
                    return null;
                }

                Ticket newTicket = new Ticket();
                newTicket.setEvent(event);
                newTicket.setUser(user);

                Ticket savedTicket = ticketRepository.save(newTicket);

                System.out.println("Sucesso! Ingresso gerado e comprado para: " + user.getName());
                return PurchaseProof.fromTicket(savedTicket);
            } finally {
                redisLock.releaseLock(lockKey, lockValue);
            }
        } else {
            System.out.println("Alta concorrência no evento. Reenfileirando requisição...");
            return null;
        }
    }
}