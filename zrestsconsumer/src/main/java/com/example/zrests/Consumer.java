package com.example.zrests;

import com.example.zrests.repository.RestaurantRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class Consumer {

    private RestaurantRepository restaurantRepository;

    public Consumer(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @KafkaListener(topics = "zrests",groupId = "king")
    public void consume(String jsonStr) throws JsonProcessingException {
        System.out.println(jsonStr);
        ObjectMapper mapper = new ObjectMapper();
        //Map<String,Object> map = mapper.readValue(json, Map.class);
        Request1 r = mapper.readValue(jsonStr,Request1.class);
        if(Objects.equals(r.getType(), "Add")) {
            Restaurant restaurant = r.getRestaurant();
            restaurantRepository.save(restaurant);
        } else if (r.getType()=="ListAll") {
            //System.out.println(restaurantRepository.findAll());
        }else {
            // TODO:// getByID, UpdateByID, deleteByID
        }
    }
}
