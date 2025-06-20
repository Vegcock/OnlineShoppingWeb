package com.web.mapper;

import com.web.entity.OnsaleComd;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OnsaleComdMapper {
    @Insert("insert into onsale(name,description,status,price,url,comd_id) values (#{name},#{description},#{status},#{price},#{url},#{comdId})")
    void add(OnsaleComd onsaleComd);

    @Delete("delete from onsale where id = #{id}")
    void remove(Integer id);

    @Delete("delete from onsale where comd_id = #{id}")
    void removeByComd(Integer id);

    @Update("update onsale set name = #{name},description = #{description},status = #{status},price = #{price},url = #{url} where id = #{id}")
    void edit(OnsaleComd onsaleComd);

    @Select("SELECT id, name, description, status, price, url, comd_id FROM onsale")
    List<OnsaleComd> list();

    @Select("select id,name,price,url,status,description,comd_id from onsale where id = #{id}")
    OnsaleComd search(Integer id);
}
