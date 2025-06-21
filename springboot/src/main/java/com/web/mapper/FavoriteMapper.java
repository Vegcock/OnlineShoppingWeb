package com.web.mapper;

import com.web.entity.Favorite;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FavoriteMapper {

    @Insert("INSERT INTO favorite(user_id, commodity_id, create_time) " +
            "VALUES(#{userId}, #{commodityId}, NOW())")
    void add(@Param("userId") String userId, @Param("commodityId") Integer commodityId);

    @Delete("DELETE FROM favorite WHERE user_id = #{userId} AND commodity_id = #{commodityId}")
    void delete(@Param("userId") String userId, @Param("commodityId") Integer commodityId);

    @Select("SELECT * FROM favorite WHERE user_id = #{userId}")
    List<Favorite> listByUser(@Param("userId") String userId);

    @Select("SELECT COUNT(*) FROM favorite WHERE user_id = #{userId} AND commodity_id = #{commodityId}")
    int countByUserAndCommodity(@Param("userId") String userId, @Param("commodityId") Integer commodityId);

}
