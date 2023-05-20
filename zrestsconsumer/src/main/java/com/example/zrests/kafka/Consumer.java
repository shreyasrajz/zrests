package com.example.zrests.kafka;

import com.example.zrests.Request1;
import com.example.zrests.Restaurant;
import com.example.zrests.repository.RestaurantRepository;
import com.example.zrests.service.RestaurantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class Consumer {

    private RestaurantService restaurantService;

    public Consumer(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @KafkaListener(topics = "zrests",groupId = "king")
    public void consume(String jsonStr) throws JsonProcessingException {
        System.out.println(jsonStr);
        ObjectMapper mapper = new ObjectMapper();
        //Map<String,Object> map = mapper.readValue(json, Map.class);
        Request1 r = mapper.readValue(jsonStr,Request1.class);
        Long id = r.getId();
        if(Objects.equals(r.getType(), "Add")) {
            Restaurant restaurant = r.getRestaurant();
            restaurantService.saveRestaurant(restaurant);
        } else if (Objects.equals(r.getType(), "ListAll")) {
            System.out.println(restaurantService.listAllRestaurants());
        }else if(Objects.equals(r.getType(), "ListByID")) {
            // TODO:// getByID, UpdateByID, deleteByID
            Restaurant restaurant = restaurantService.listByID(id);
            System.out.println(restaurant.toString());
        } else if(Objects.equals(r.getType(), "deleteByID")) {
            restaurantService.deleteByID(id);
        }
    }
}
