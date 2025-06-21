package com.web.service.impl;

import com.web.entity.Favorite;
import com.web.mapper.FavoriteMapper;
import com.web.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public void add(String userId, Integer commodityId) {
        favoriteMapper.add(userId, commodityId);
    }

    @Override
    public void delete(String userId, Integer commodityId) {
        favoriteMapper.delete(userId, commodityId);
    }

    @Override
    public List<Favorite> getByUser(String userId) {
        return favoriteMapper.listByUser(userId);
    }

    @Override
    public boolean isFavorited(String userId, Integer commodityId) {
        return favoriteMapper.countByUserAndCommodity(userId, commodityId) > 0;
    }
}
