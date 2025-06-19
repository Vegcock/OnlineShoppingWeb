package com.web.mapper;

import com.web.entity.AjaxResult;
import com.web.entity.Commodity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommodityMapper {

    @Insert("INSERT INTO commodity(name, brandName, companyName, description, status, account, price) " +
            "VALUES (#{name}, #{brandName}, #{companyName}, #{description}, #{status}, #{account}, #{price})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Commodity commodity);


    @Delete("delete from commodity where id = #{id}")
    void remove(Integer id);

    @Update("update commodity set name = #{name}, brandName = #{brandName}, companyName = #{companyName}, description = #{description}, status = #{status}, price = #{price}, account = #{account} where id = #{id}")
    void edit(Commodity commodity);

    @Select("select * from commodity")
    List<Commodity> list();

    @Select("select id,name,brandName,companyName,description,status,price,account from commodity where id = #{id}")
    Commodity search(Integer id);
}
