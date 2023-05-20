package com.example.zrests.service;

import com.example.zrests.Restaurant;
import com.example.zrests.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    public void saveRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public List<Restaurant> listAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant listByID(Long id) {
        return restaurantRepository.findById(id).get();
    }

    public void deleteByID(Long id) {
        restaurantRepository.deleteById(id);
    }
}
