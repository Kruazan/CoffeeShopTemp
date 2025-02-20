package com.example.coffeeshop.model;

/** nothing now. */
public class Coffee {
    private Long id;
    private String type;
    private String size;

    /** nothing now. */
    public Coffee() {}

    /** nothing now. */
    public Coffee(Long id, String type, String size) {
        this.id = id;
        this.type = type;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}