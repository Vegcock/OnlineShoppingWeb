package com.web.service.impl;

import com.web.DTO.OrderDetailDTO;
import com.web.mapper.OrderViewMapper;
import com.web.service.OrderViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderViewServiceImpl implements OrderViewService {

    @Autowired
    private OrderViewMapper orderViewMapper;

    @Override
    public OrderDetailDTO getOrderDetail(int orderId) {
        return orderViewMapper.selectOrderDetail(orderId);
    }
}
