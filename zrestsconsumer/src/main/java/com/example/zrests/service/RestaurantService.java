package com.example.zrests.service;

import com.example.zrests.model.Restaurant;
import com.example.zrests.repository.RestaurantRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantService.class);
    @Autowired
    private RestaurantRepository restaurantRepository;

    JedisPooled jedis = new JedisPooled("localhost", 6379);
    final Gson gson = new Gson();

    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> listAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant listByID(Long id) throws JsonProcessingException {
        Restaurant restaurant;
        String key = "restaurant:"+id.toString();
        boolean idPresentInRedis = jedis.exists(key);
        if(idPresentInRedis) {
            LOGGER.info("Fetching from Redis");
            String value = jedis.get(key);
            ObjectMapper mapper = new ObjectMapper();
            restaurant = mapper.readValue(value,Restaurant.class);
        }else {
            System.out.println(String.format("%s is not present in the redis",key));
            Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
            if(restaurantOptional.isPresent()) {
                restaurant = restaurantOptional.get();
                LOGGER.info("Adding to redis");
                jedis.set("restaurant:"+ id,gson.toJson(restaurant));
            }else {
                restaurant = new Restaurant();
                LOGGER.info("Restaurant with given is not found");
            }
        }
        return restaurant;
    }

    public boolean deleteByID(Long id) {
        String key = "restaurant:"+id.toString();
        boolean idPresentInRedis = jedis.exists(key);
        restaurantRepository.deleteById(id);
        if(idPresentInRedis) {
            jedis.del(key);
        }
        System.out.println("Deleted");
        return true;
    }

    public Restaurant updateByID(Long id, Restaurant newRestaurant) {
        String key = "restaurant:"+id.toString();
        boolean idPresentInRedis = jedis.exists(key);
        if(idPresentInRedis) {
            LOGGER.info("Fetching from Redis");
            jedis.del(key);
        }
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        if(restaurantOptional.isPresent()) {
            Restaurant oldRestaurant = restaurantOptional.get();
            oldRestaurant.setCity(newRestaurant.getCity());
            oldRestaurant.setEmail(newRestaurant.getEmail());
            oldRestaurant.setName(newRestaurant.getName());
            oldRestaurant.setPhone(newRestaurant.getPhone());
            LOGGER.info("Updating the entity");
            return restaurantRepository.save(oldRestaurant);
        }else {
            return new Restaurant();
        }
    }
}
