package com.web.mapper;

import com.web.entity.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {
    @Insert("insert into cart(name,commodity,description,accounts,price) values (#{name},#{commodity},#{description},#{accounts},#{price})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Cart cart);

    @Delete("delete from cart where id = #{id}")
    void remove(Integer id);

    @Update("update cart set name = #{name},commodity = #{commodity},description = #{description},accounts = #{accounts},price = #{price} where id = #{id}")
    void edit(Cart cart);

    @Select("select * from cart")
    List<Cart> list();

    @Select("select id,name,commodity,description,accounts,price from cart where id = #{id}")
    Cart search(Integer id);
}
