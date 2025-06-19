package com.web.service;

import com.web.entity.AjaxResult;
import com.web.entity.Customer;
import com.web.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserOrderService {
    List<Order> getOrderByUserId(String userId);
    Customer getCustomerByOrderId(int orderId);
    AjaxResult addOrderRelation(String userId, int orderId);
    AjaxResult deleteOrderRelation(String userId, Integer orderId);
    AjaxResult updateOrderRelation(String userId, Integer oldOrderId, Integer newOrderId);
}
