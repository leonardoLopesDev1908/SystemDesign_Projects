package com.tickets.controller.dto.response;

import com.tickets.entities.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
    private String name;
    private String date;

    public static EventResponse toResponse(Event event){
        return new EventResponse(event.getName(), event.getTimestamp().toString());
    }
}
