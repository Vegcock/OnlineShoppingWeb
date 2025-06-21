package com.web.controller;

import com.web.Utils.RedisUtil;
import com.web.entity.AjaxResult;
import com.web.entity.Order;
import com.web.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // 允许跨域
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/od/list")
    public AjaxResult list(){
        String cacheKey = "order:list:all";

        if(redisUtil.hasKey(cacheKey)){
            List<Order> orders = (List<Order>) redisUtil.get(cacheKey);
            return AjaxResult.success(orders);
        }

        List<Order> orderList = orderService.list();
        redisUtil.set(cacheKey,orderList,10);
        return AjaxResult.success(orderList);
    }

    @DeleteMapping("/od/{id}")
    public AjaxResult remove(@PathVariable Integer id){
        return orderService.remove(id);
    }

    @PostMapping("/od")
    public AjaxResult add(@RequestBody Order order){
        return orderService.add(order);
    }

    @PutMapping("/od")
    public AjaxResult edit(@RequestBody Order order){
        return orderService.edit(order);
    }

    @PostMapping("/od/pay")
    public AjaxResult payOrders(@RequestBody PayRequest request) {
        orderService.updateStatusBatch(request.getOrderIds(), request.getStatus());
        return AjaxResult.success("支付成功");
    }

    @PutMapping("/od/p")
    public AjaxResult updateStatus(@RequestParam("id") Integer id,@RequestParam("status") String status){
        return orderService.updatePaidStatus(id,status);
    }

    @PutMapping("/od/d")
    public AjaxResult updateDeliveryStatus  (@RequestParam("id") Integer id,@RequestParam("status") String status){
        return orderService.updateDeliveryStatus(id,status);
    }

    @GetMapping("/od/{id}")
    public AjaxResult search(@PathVariable Integer id){
        Order order = orderService.search(id);
        return AjaxResult.success(order);
    }
}
class PayRequest {
    private List<Integer> orderIds;
    private String status;

    public List<Integer> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Integer> orderIds) {
        this.orderIds = orderIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}