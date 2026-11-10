package com.tickets.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tickets.entities.Event;
import com.tickets.repository.EventRepository;

@Service
public class GeneralService {
    
    private EventRepository eventRepository;
 
    public GeneralService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAll(){
        return eventRepository.findAll();
    }
}
