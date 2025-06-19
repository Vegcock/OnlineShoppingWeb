package com.web.service.impl;

import com.web.entity.AjaxResult;
import com.web.entity.Order;
import com.web.mapper.OrderMapper;
import com.web.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public AjaxResult add(Order order) {
        orderMapper.add(order);
        Integer insertedId = order.getId();
        return AjaxResult.success(insertedId);
    }

    @Override
    public AjaxResult remove(Integer id) {
        orderMapper.remove(id);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult edit(Order order) {
        orderMapper.edit(order);
        return AjaxResult.success();
    }

    @Override
    public void updateStatusBatch(List<Integer> orderIds, String status) {
        for (Integer id : orderIds) {
            orderMapper.updatePaidStatus(id, status);
        }
    }

    @Override
    public AjaxResult updatePaidStatus(Integer id, String status) {
        orderMapper.updatePaidStatus(id, status);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult updateDeliveryStatus(Integer id, String deliveryStatus) {
        orderMapper.updateDeliveryStatus(id, deliveryStatus);
        return AjaxResult.success();
    }

    @Override
    public List<Order> list() {
        return orderMapper.list();
    }

    @Override
    public Order search(Integer id) {
        return orderMapper.search(id);
    }
}
