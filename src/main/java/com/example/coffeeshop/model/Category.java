package com.example.coffeeshop.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Category {

    @Id
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<Coffee> coffees;

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Coffee> getCoffees() {
        return coffees;
    }

    public void setCoffees(List<Coffee> coffees) {
        this.coffees = coffees;
    }
}
