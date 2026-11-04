package com.tickets.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Event {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    LocalDateTime timestamp;
}
