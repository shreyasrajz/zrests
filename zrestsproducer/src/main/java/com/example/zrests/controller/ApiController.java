package com.example.zrests.controller;

import com.example.zrests.kafka.Producer;
import com.example.zrests.model.Restaurant;
import com.example.zrests.requests.Request2;
import com.example.zrests.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ApiController {

    private Producer producer;
    private RestaurantService restaurantService;

    public ApiController(Producer producer,RestaurantService restaurantService) {
        this.producer = producer;
        this.restaurantService=restaurantService;
    }
    @PostMapping("/add")
    public String add(@RequestBody Restaurant restaurant,@RequestHeader(value = "sync",defaultValue = "false") Boolean sync) {
        if(sync) {
            System.out.println(restaurantService.save(restaurant));
            System.out.println("A synchronous request is made");
            return "sync ok";
        }
        Request2 request = new Request2();
        request.setType("Add");
        request.setRestaurant(restaurant);
        producer.sendmessage(request);
        return "ok";
    }
    @GetMapping(value = "/listall")
    public String listAll(HttpServletRequest httpServletRequest,@RequestHeader(value = "sync",defaultValue = "false")Boolean sync) {
        if(sync) {
            System.out.println(restaurantService.listAll());
            System.out.println("A synchronous request is made");
            return "sync ok";
        }
        System.out.println(httpServletRequest.getHeaderNames().toString());
        Request2 request = new Request2();
        request.setType("ListAll");
        producer.sendmessage(request);
        System.out.println("Request to list all the Restaurants");
        return "ok";
    }
    @GetMapping("/list/{id}")
    public String listByID(@PathVariable("id") Long id,@RequestHeader(value = "sync",defaultValue = "false") Boolean sync) {
        if(sync) {
            System.out.println(restaurantService.listByID(id));
            System.out.println("A synchronous request is made");
            return "sync ok";
        }
        Request2 request = new Request2();
        request.setType("ListByID");
        request.setId(id);
        producer.sendmessage(request);
        System.out.println(String.format("Request to list Restaurant with id: %s",id));
        return "ok";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteByID(@PathVariable("id") Long id,@RequestHeader(value = "sync",defaultValue = "false") Boolean sync) {
        if(sync) {
            System.out.println(restaurantService.deleteByID(id));
            System.out.println("A synchronous request is made");
            return "sync ok";
        }
        Request2 request = new Request2();
        request.setType("DeleteByID");
        request.setId(id);
        producer.sendmessage(request);
        System.out.println(String.format("Request to delete restaurant with id: %s",id));
        return "ok";
    }
    @GetMapping("/hello")
    public String hello(@RequestHeader(value="User-Agent") String userAgent) {
        System.out.println(userAgent);
        return "hello";
    }
}
