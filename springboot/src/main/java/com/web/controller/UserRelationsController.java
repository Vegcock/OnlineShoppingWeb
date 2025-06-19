package com.web.controller;

import com.web.entity.AjaxResult;
import com.web.entity.Cart;
import com.web.entity.Customer;
import com.web.entity.Order;
import com.web.service.OrderService;
import com.web.service.UserCartService;
import com.web.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // 允许跨域
public class UserRelationsController {
    @Autowired
    private UserCartService userCartService;

    @Autowired
    private UserOrderService userOrderService;

    @RequestMapping("/uc")
    public AjaxResult clist(@RequestParam("UserId") String userId) {
        List<Cart> cartList = userCartService.getUserCartByUserId(userId);
        return AjaxResult.success(cartList);
    }

    @RequestMapping("/uc/add")
    public AjaxResult addCartRelation(@RequestParam("UserId") String userId,@RequestParam("CartId")Integer cartId){
        return userCartService.addCartRelation(userId,cartId);
    }

    @RequestMapping("/uo")
    public AjaxResult olist(@RequestParam("UserId") String userId){
        List<Order> orderList = userOrderService.getOrderByUserId(userId);
        return AjaxResult.success(orderList);
    }

    @RequestMapping("/ou")
    public AjaxResult getCustomer(@RequestParam("OrderId") int orderId){
        Customer customer = userOrderService.getCustomerByOrderId(orderId);
        return AjaxResult.success(customer);
    }

    @PostMapping("/uo/add")
    public AjaxResult addOrderRelation(@RequestBody Map<String, Object> map) {
        String userId = (String) map.get("UserId");
        Integer orderId = (Integer) map.get("OrderId");
        return userOrderService.addOrderRelation(userId, orderId);
    }

}
