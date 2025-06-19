package com.web.service;

import com.web.entity.AjaxResult;
import com.web.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    AjaxResult add(Order order);
    AjaxResult remove(Integer id);
    AjaxResult edit(Order order);
    AjaxResult updatePaidStatus(Integer id, String status);
    AjaxResult updateDeliveryStatus(Integer id, String deliveryStatus);
    List<Order> list();
    Order search(Integer id);
    void updateStatusBatch(List<Integer> orderIds, String status);
}
