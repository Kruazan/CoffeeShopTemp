package com.example.coffeeshop.model;

import lombok.Getter;
import lombok.Setter;

/** Coffee model. */
@Setter
@Getter
public class Coffee {
    private Long id;
    private String name;
    private String type;
    private double price;

    /** Coffee constructor. */
    public Coffee(Long id, String name, String type, double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
    }

}
