package com.web.mapper;

import com.web.entity.Commodity;
import com.web.entity.Customer;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CustomerMapper {
    @Select("SELECT * FROM customer WHERE email = #{email} LIMIT 1")
    Customer selectByEmail(@Param("email") String email);

    @Select("SELECT * FROM customer WHERE phone = #{phone} LIMIT 1")
    Customer selectByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM customer WHERE user_id = #{id}")
    Customer selectById(String id);

    @Select("select * from customer")
    List<Customer> list();

    @Insert("INSERT INTO customer(user_id, name, phone, email, address, place, vip, create_time, status, url) " +
            "VALUES(#{userId}, #{name}, #{phone}, #{email}, #{address}, #{place}, #{vip}, #{createTime}, #{status}, #{url})")
    @Options(useGeneratedKeys = false, keyProperty = "userId") // 禁用自增，因为我们自定义ID
    int insert(Customer customer);

    // 2. 根据ID删除用户
    @Delete("DELETE FROM customer WHERE user_id = #{userId}")
    int deleteById(String userId);

    @Update("UPDATE customer SET " +
            "name = #{name}, " +
            "phone = #{phone}, " +
            "email = #{email}, " +
            "address = #{address}, " +
            "place = #{place}, " +
            "vip = #{vip}, " +
            "status = #{status}, " +
            "url = #{url} " +
            "WHERE user_id = #{userId}")
    int update(Customer customer);
}
