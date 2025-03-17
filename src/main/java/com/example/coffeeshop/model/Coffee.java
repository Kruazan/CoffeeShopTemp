package com.example.coffeeshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/** Coffee model. */
@Setter
@Getter
@Entity
@Table(name = "coffees")
public class Coffee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private double price;
}
