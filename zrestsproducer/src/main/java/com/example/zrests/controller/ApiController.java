package com.example.zrests.controller;

import com.example.zrests.kafka.Producer;
import com.example.zrests.model.Restaurant;
import com.example.zrests.requests.Request2;
import com.example.zrests.service.RestaurantService;
import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);
    private Producer producer;
    private RestaurantService restaurantService;

    public ApiController(Producer producer,RestaurantService restaurantService) {
        this.producer = producer;
        this.restaurantService=restaurantService;
    }
    @PostMapping("/add")
    public String add(@RequestBody Restaurant restaurant,
                      @RequestHeader(value = "sync",defaultValue = "false") Boolean sync) {
        if(sync) {
            LOGGER.info("Synchronous add request is made");
            Restaurant saved = restaurantService.save(restaurant);
            return "Synchronous Request \n"+
                    saved.toString() +
                    "\nis added to the database";
        }
        Request2 request = new Request2();
        request.setType("Add");
        request.setRestaurant(restaurant);
        producer.sendmessage(request);
        return "ok";
    }
    @GetMapping(value = "/listall")
    public String listAll(HttpServletRequest httpServletRequest,
                          @RequestHeader(value = "sync",defaultValue = "false")Boolean sync) {
        if(sync) {
            List<Restaurant> restaurantList = restaurantService.listAll();
            System.out.println(restaurantList);
            return "Synchronous Request \n"+
                    "List of restaurants: \n"+
                    restaurantList.toString();
        }
        System.out.println(httpServletRequest.getHeaderNames().toString());
        Request2 request = new Request2();
        request.setType("ListAll");
        producer.sendmessage(request);
        System.out.println("Request to list all the Restaurants");
        return "ok";
    }
    @GetMapping("/list/{id}")
    public String listByID(@PathVariable("id") Long id,
                           @RequestHeader(value = "sync",defaultValue = "false") Boolean sync) {
        if(sync) {
            Restaurant restaurant = restaurantService.listByID(id);
            if(restaurant.getId() == null) {
                return String.format(
                        "Synchronous Request \n"+
                                "Restaurant with id: %s is not found",id);
            }
            return String.format(
                    "Synchronous Request \n"+
                    "Restaurant with id: {} \n",id) +
                    restaurant.toString();
        }
        Request2 request = new Request2();
        request.setType("ListByID");
        request.setId(id);
        producer.sendmessage(request);
        System.out.println(String.format("Request to list Restaurant with id: %s",id));
        return "ok";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteByID(@PathVariable("id") Long id,
                             @RequestHeader(value = "sync",defaultValue = "false") Boolean sync) {
        if(sync) {
            restaurantService.deleteByID(id);
            return String.format(
                    "Synchronous Request \n"+
                            "Restaurant with id: %s is deleted",id);
        }
        Request2 request = new Request2();
        request.setType("DeleteByID");
        request.setId(id);
        producer.sendmessage(request);
        System.out.println(String.format("Request to delete restaurant with id: %s",id));
        return "ok";
    }
    @PutMapping("/update/{id}")
    public String updateByID(@PathVariable("id") Long id,
                             @RequestHeader(value = "sync",defaultValue = "false") Boolean sync,
                             @RequestBody Restaurant restaurant) {
        if(sync) {
            restaurant = restaurantService.updateById(id,restaurant);
            if(restaurant.getId()==null) {
                return "Synchronous Request \n"+
                        "Restaurant not found";
            }
            return String.format(
                    "Synchronous Request \n"+
                            "Restaurant with id: %s is updated \n",id) +
                    restaurant;
        }
        Request2 request = new Request2();
        request.setType("UpdateByID");
        request.setId(id);
        request.setRestaurant(restaurant);
        producer.sendmessage(request);
        System.out.println(String.format("Request to Update restaurant with id: %s",id));
        return "ok";
    }
    @GetMapping("/hello")
    public String hello(@RequestHeader(value="User-Agent") String userAgent) {
        System.out.println(userAgent);
        return "hello";
    }
}
