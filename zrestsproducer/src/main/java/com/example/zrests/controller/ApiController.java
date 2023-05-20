package com.example.zrests.controller;

import com.example.zrests.kafka.Producer;
import com.example.zrests.model.Restaurant;
import com.example.zrests.requests.Request2;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiController {

    private Producer producer;

    public ApiController(Producer producer) {
        this.producer = producer;
    }
    @PostMapping("/add")
    public String add(@RequestBody Restaurant restaurant,@RequestHeader(value = "sync") Boolean sync) {
        Request2 request = new Request2();
        request.setType("Add");
        request.setRestaurant(restaurant);
        producer.sendmessage(request);
        return "ok";
    }
    @GetMapping("/listall")
    public String listAll(@RequestHeader(value = "sync") Boolean sync) {
        if(sync) {
            System.out.println("A synchronous request is made");
            return "sync ok";
        }
        Request2 request = new Request2();
        request.setType("ListAll");
        producer.sendmessage(request);
        System.out.println("Request to list all the Restaurants");
        return "ok";
    }
    @GetMapping("/list/{id}")
    public String listByID(@PathVariable("id") Long id,@RequestHeader(value = "sync") Boolean sync) {
        Request2 request = new Request2();
        request.setType("ListByID");
        request.setId(id);
        producer.sendmessage(request);
        System.out.println(String.format("Request to list Restaurant with id: %s",id));
        return "ok";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteByID(@PathVariable("id") Long id,@RequestHeader(value = "sync") Boolean sync) {
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
