package com.example.project4.controller;
import com.example.project4.service.DeliveryTrackingService;
import com.example.project4.entity.DeliveryTracking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/tracking")
@CrossOrigin

public class DeliveryTrackingController {

    @Autowired
    private DeliveryTrackingService deliveryTrackingService;

    @PostMapping
    public DeliveryTracking save(@RequestBody DeliveryTracking deliveryTracking){
        return deliveryTrackingService.save(deliveryTracking);
    }
    @GetMapping("/{deliveryId}")
    public DeliveryTracking getLastest(@PathVariable String deliveryId){
        return deliveryTrackingService.getLastest(deliveryId);
    }

}
