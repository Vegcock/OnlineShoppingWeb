package com.web.mapper;

import com.web.DTO.OrderDetailDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderViewMapper {
    @Select("SELECT * FROM user_orders WHERE order_id = #{orderId}")
    @Results(id = "orderDetailMap", value = {
            @Result(property = "order", column = "order_id",
                    one = @One(select = "com.web.mapper.OrderMapper.search")),
            @Result(property = "customer", column = "user_id",
                    one = @One(select = "com.web.mapper.CustomerMapper.selectById")),
            @Result(property = "delivery", column = "order_id",
                    one = @One(select = "com.web.mapper.DeliveryMapper.getByOrderId"))
    })
    OrderDetailDTO selectOrderDetail(@Param("orderId") int orderId);
}
