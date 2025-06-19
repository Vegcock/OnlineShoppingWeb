package com.web.controller;

import com.web.entity.AjaxResult;
import com.web.entity.Delivery;
import com.web.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @RequestMapping("/dr/list")
    public AjaxResult list() {
        List<Delivery> deliveries = deliveryService.list();
        return AjaxResult.success(deliveries);
    }

    @PostMapping("/dr")
    public AjaxResult add(@RequestBody Delivery delivery) {
        return deliveryService.add(delivery);
    }

    @PostMapping("/dr/batch")
    public AjaxResult createDeliveries(@RequestBody List<Delivery> deliveryList) {
        for (Delivery delivery : deliveryList) {
            deliveryService.add(delivery);  // 你原有的单个插入方法
        }
        return AjaxResult.success("物流单生成成功");
    }

    @DeleteMapping("/dr")
    public AjaxResult remove(@RequestParam Integer id) {
        return deliveryService.remove(id);
    }

    @PutMapping("/dr")
    public AjaxResult edit(@RequestBody Delivery delivery) {
        return deliveryService.edit(delivery);
    }

    @GetMapping("/dr/id")
    public AjaxResult search(@RequestParam Integer id) {
        return AjaxResult.success(deliveryService.search(id));
    }

    @GetMapping("/dr/oid")
    public AjaxResult getByOrderId(@RequestParam Integer orderId) {
        return AjaxResult.success(deliveryService.getByOrderId(orderId));
    }
}
