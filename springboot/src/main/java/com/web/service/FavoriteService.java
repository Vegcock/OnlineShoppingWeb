package com.web.service;

import com.web.entity.Favorite;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FavoriteService {
    void add(String userId, Integer commodityId);
    void delete(String userId, Integer commodityId);
    List<Favorite> getByUser(String userId);
    boolean isFavorited(String userId, Integer commodityId);
}
