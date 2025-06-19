package com.web.mapper;

import com.web.entity.Delivery;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface DeliveryMapper {

    @Insert("INSERT INTO delivery(delivery_id, order_id, order_date, receiver_name, address, phone, company, ship_time, status, status_update_time, remarks, poster_name) " +
            "VALUES (#{deliveryId}, #{orderId}, #{orderDate}, #{receiverName}, #{address}, #{phone}, #{company}, #{shipTime}, #{status}, #{statusUpdateTime}, #{remarks}, #{posterName})")
    void add(Delivery delivery);

    @Delete("DELETE FROM delivery WHERE id = #{id}")
    void remove(Integer id);

    @Update("UPDATE delivery SET delivery_id=#{deliveryId}, order_id=#{orderId}, order_date=#{orderDate}, receiver_name=#{receiverName}, address=#{address}, " +
            "phone=#{phone}, company=#{company}, ship_time=#{shipTime}, status=#{status}, status_update_time=#{statusUpdateTime}, remarks=#{remarks}, poster_name=#{posterName},updated_at=NOW() " +
            "WHERE id = #{id}")
    void edit(Delivery delivery);

    @Select("SELECT * FROM delivery WHERE id = #{id}")
    Delivery search(Integer id);

    @Select("SELECT * FROM delivery")
    List<Delivery> list();

    @Select("SELECT * FROM delivery WHERE order_id = #{orderId}")
    Delivery getByOrderId(Integer orderId);
}
