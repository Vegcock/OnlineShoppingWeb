package com.web.service.impl;

import com.web.entity.AjaxResult;
import com.web.entity.Delivery;
import com.web.mapper.DeliveryMapper;
import com.web.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryMapper deliveryMapper;

    @Override
    public AjaxResult add(Delivery delivery) {
        deliveryMapper.add(delivery);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult remove(Integer id) {
        deliveryMapper.remove(id);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult edit(Delivery delivery) {
        deliveryMapper.edit(delivery);
        return AjaxResult.success();
    }

    @Override
    public List<Delivery> list() {
        return deliveryMapper.list();
    }

    @Override
    public Delivery search(Integer id) {
        return deliveryMapper.search(id);
    }

    @Override
    public Delivery getByOrderId(Integer orderId) {
        return deliveryMapper.getByOrderId(orderId);
    }
}

