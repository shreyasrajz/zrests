package com.example.zrests.requests;

import com.example.zrests.model.Restaurant;

public class Request2 {
    private String type;
    private Restaurant restaurant;
    private Long id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
