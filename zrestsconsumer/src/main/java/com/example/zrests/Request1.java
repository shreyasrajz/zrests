package com.example.zrests;

public class Request1 {
    private String type;
    private Restaurant restaurant;
    private int id;

    @Override
    public String toString() {
        return "Request{" +
                "type='" + type + '\'' +
                ", restaurant=" + restaurant +
                ", id=" + id +
                '}';
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
