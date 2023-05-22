package com.example.zrests.service;

import com.example.zrests.model.Restaurant;
import com.example.zrests.repository.RestaurantRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RestaurantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantService.class);
    private RestaurantRepo restaurantRepo;

    JedisPooled jedis = new JedisPooled("localhost", 6379);
    final Gson gson = new Gson();

    public RestaurantService(RestaurantRepo restaurantRepo) {
        this.restaurantRepo = restaurantRepo;
    }

    public boolean deleteByID(Long id) {
        try {
            String key = "restaurant:"+id.toString();
            boolean idPresentInRedis = jedis.exists(key);
            restaurantRepo.deleteById(id);
            if(idPresentInRedis) {
                jedis.del(key);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public Restaurant listByID(Long id) {
        Restaurant restaurant;
        try {
            String key = "restaurant:"+id.toString();
            boolean idPresentInRedis = jedis.exists(key);
            if(idPresentInRedis) {
                String value = jedis.get(key);
                System.out.println(value);
                ObjectMapper mapper = new ObjectMapper();
                restaurant = mapper.readValue(value,Restaurant.class);
            }else {
                System.out.println(String.format("%s is not present in the redis",key));
                Optional<Restaurant> restaurantOptional = restaurantRepo.findById(id);
                if(restaurantOptional.isPresent()) {
                    restaurant = restaurantOptional.get();
                    jedis.set("restaurant:"+ id,gson.toJson(restaurant));
                }else {
                    restaurant = new Restaurant();
                    LOGGER.info("Restaurant with given is not found");
                }
            }
            return restaurant;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Restaurant> listAll() {
        LOGGER.info("Listing all the restaurants");
        return restaurantRepo.findAll();
    }

    public Restaurant save(Restaurant restaurant) {
        LOGGER.info("Adding new restaurant to database");
        return restaurantRepo.save(restaurant);
    }

    public Restaurant updateById(Long id, Restaurant newRestaurant) {
        String key = "restaurant:"+id.toString();
        boolean idPresentInRedis = jedis.exists(key);
        if(idPresentInRedis) {
            LOGGER.info("Deleting the old key");
            jedis.del(key);
        }

        Optional<Restaurant> restaurantOptional = restaurantRepo.findById(id);
        if(restaurantOptional.isPresent()) {
            Restaurant oldRestaurant = restaurantOptional.get();
            oldRestaurant.setCity(newRestaurant.getCity());
            oldRestaurant.setEmail(newRestaurant.getEmail());
            oldRestaurant.setName(newRestaurant.getName());
            oldRestaurant.setPhone(newRestaurant.getPhone());
            LOGGER.info("Updating the entity");
            return restaurantRepo.save(oldRestaurant);
        }else {
            return new Restaurant();
        }
    }
}
