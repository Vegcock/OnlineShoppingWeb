package com.web.service;

import com.web.entity.AjaxResult;
import com.web.entity.Delivery;

import java.util.List;

public interface DeliveryService {
    AjaxResult add(Delivery delivery);
    AjaxResult remove(Integer id);
    AjaxResult edit(Delivery delivery);
    List<Delivery> list();
    Delivery search(Integer id);
    Delivery getByOrderId(Integer orderId);
}
