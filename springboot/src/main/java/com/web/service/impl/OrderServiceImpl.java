package com.web.service.impl;

import com.web.DAO.UserOrderDao;
import com.web.Utils.RedisUtil;
import com.web.entity.AjaxResult;
import com.web.entity.Commodity;
import com.web.entity.Order;
import com.web.mapper.CommodityMapper;
import com.web.mapper.OrderMapper;
import com.web.service.CommodityService;
import com.web.service.OrderService;
import com.web.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private CommodityMapper commodityMapper;

    private UserOrderDao userOrderDao = new UserOrderDao();

    @Override
    public AjaxResult add(Order order) {
        Commodity commodity = commodityService.search(order.getCommodityId());
        if (commodity == null) {
            return AjaxResult.error("商品不存在");
        }
        if (commodity.getAccount() <= 0) {
            return AjaxResult.error("商品已售罄");
        }
        // 扣减库存
        commodity.setAccount(commodity.getAccount() - 1);
        commodityMapper.edit(commodity);

        String cacheKey = "commodity:list";
        if (redisUtil.hasKey(cacheKey)) {
            List<Commodity> cachedList = (List<Commodity>) redisUtil.get(cacheKey);
            for (Commodity item : cachedList) {
                if (item.getId().equals(order.getCommodityId())) {
                    item.setAccount(item.getAccount() - 1);
                    break;
                }
            }
            redisUtil.set(cacheKey, cachedList, 600); // 重新缓存 10 分钟
        }

        orderMapper.add(order);
        Integer insertedId = order.getId();
        return AjaxResult.success(insertedId);
    }

    @Override
    public AjaxResult remove(Integer id) {
        orderMapper.remove(id);
        userOrderDao.deleteOrderOnly(id);
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
