package com.web.controller;

import com.web.DTO.OrderDetailDTO;
import com.web.entity.AjaxResult;
import com.web.service.OrderViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*") // 允许跨域
public class OrderViewController {

    @Autowired
    private OrderViewService orderViewService;

    @GetMapping("/order/detail/{orderId}")
    public AjaxResult getOrderDetail(@PathVariable int orderId) {
        OrderDetailDTO dto = orderViewService.getOrderDetail(orderId);
        return AjaxResult.success(dto);
    }
}
