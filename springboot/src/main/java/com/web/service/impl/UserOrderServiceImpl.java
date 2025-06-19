package com.web.service.impl;

import com.web.DAO.UserOrderDao;
import com.web.entity.AjaxResult;
import com.web.entity.Customer;
import com.web.entity.Order;
import com.web.mapper.CustomerMapper;
import com.web.mapper.OrderMapper;
import com.web.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserOrderServiceImpl implements UserOrderService {
    private UserOrderDao userOrderDao = new UserOrderDao();

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CustomerMapper customerMapper;

    public List<Order> getOrderByUserId(String userId){
        List<Integer> orderIds = userOrderDao.findOrderIdsByUserId(userId);
        List<Order> orders = new ArrayList<>(orderIds.size());
        for (Integer orderId : orderIds) {
            orders.add(orderMapper.search(orderId));
        }
        return orders;
    }

    public Customer getCustomerByOrderId(int orderId){
        return customerMapper.selectById(userOrderDao.findUserIdByOrderId(orderId));
    }

    public AjaxResult addOrderRelation(String userId,int orderId){
        return userOrderDao.addOrderRelation(userId,orderId);
    }

    @Override
    public AjaxResult deleteOrderRelation(String userId, Integer orderId) {
        return userOrderDao.deleteOrderRelation(userId,orderId);
    }

    @Override
    public AjaxResult updateOrderRelation(String userId, Integer oldOrderId, Integer newOrderId) {
        return userOrderDao.updateOrderRelation(userId,oldOrderId,newOrderId);
    }


}
