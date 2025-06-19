package com.web.mapper;

import com.web.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("insert into orders(name,commodity_id,description,accounts,status,total_price,delivery_status,create_time) values (#{name},#{commodityId},#{description},#{accounts},#{status},#{price},#{deliveryStatus},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId", keyColumn = "order_id")
    void add(Order order);

    @Delete("delete from orders where order_id = #{id}")
    void remove(Integer id);

    @Delete("delete from orders where commodity_id = #{id}")
    void removeByComd(Integer id);

    @Update("update orders set name = #{name},commodityId = #{commodityId},description = #{description},accounts = #{accounts},status = #{status},price = #{price},deliveryStatus = #{deliveryStatus},createTime = #{createTime} where order_id = #{order_id}")
    void edit(Order order);

    @Update("UPDATE orders SET status = #{status} WHERE order_id = #{orderId}")
    void updatePaidStatus(Integer orderId, String status);

    @Update("UPDATE orders SET delivery_status = #{deliveryStatus} WHERE order_id = #{orderId}")
    void updateDeliveryStatus(Integer orderId, String deliveryStatus);

    @Select("SELECT * FROM orders")
    List<Order> list();

    @Select("select order_id,name,commodity_id,description,accounts,status,total_price,delivery_status,create_time from orders where order_id = #{id}")
    Order search(Integer id);
}
