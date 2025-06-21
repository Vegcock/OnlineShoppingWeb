package com.web.service;

import com.web.DTO.OrderDetailDTO;
import org.springframework.stereotype.Service;

@Service
public interface OrderViewService {
    OrderDetailDTO getOrderDetail(int orderId);
}
