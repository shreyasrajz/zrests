package com.example.zrests.service;

import com.example.zrests.model.Restaurant;
import com.example.zrests.repository.RestaurantRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private RestaurantRepo restaurantRepo;

    public RestaurantService(RestaurantRepo restaurantRepo) {
        this.restaurantRepo = restaurantRepo;
    }

    public boolean deleteByID(Long id) {
        try {
            restaurantRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public Optional<Restaurant> listByID(Long id) {
        try {
            return restaurantRepo.findById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Restaurant> listAll() {
        return restaurantRepo.findAll();
    }

    public Restaurant save(Restaurant restaurant) {
        return restaurantRepo.save(restaurant);
    }
}
