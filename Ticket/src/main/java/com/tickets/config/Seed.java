package com.tickets.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tickets.entities.Event;
import com.tickets.entities.Ticket;
import com.tickets.entities.User;
import com.tickets.repository.EventRepository;
import com.tickets.repository.TicketRepository;
import com.tickets.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Seed {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public Seed(EventRepository eventRepository, TicketRepository ticketRepository,
                UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }
    @Bean
    CommandLineRunner completeInitDatabase(EventRepository repository) {
        return args -> {
            User user1 = new User();
            user1.setName("Alice Silva");

            User user2 = new User();
            user2.setName("Bruno Souza");

            userRepository.saveAll(List.of(user1, user2));

            Event event1 = new Event();
            event1.setId(1L);
            event1.setName("Conferência de Tecnologia 2026");
            event1.setTimestamp(LocalDateTime.of(2026, 12, 10, 19, 0));

            Event event2 = new Event();
            event2.setId(2L);
            event2.setName("Workshop de Java & Spring Boot");
            event2.setTimestamp(LocalDateTime.of(2026, 11, 20, 14, 30));

            eventRepository.saveAll(List.of(event1, event2));

            Ticket ticket1 = new Ticket();
            ticket1.setEvent(event1);

            Ticket ticket2 = new Ticket();
            ticket2.setEvent(event2);

            ticketRepository.saveAll(List.of(ticket1, ticket2)); 
        };
    }
}