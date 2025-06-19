package com.web.service;

import com.web.DAO.UserCartDao;
import com.web.entity.AjaxResult;
import com.web.entity.Cart;
import com.web.entity.UserCart;
import com.web.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface UserCartService {
    List<Cart> getUserCartByUserId(String userId);
    AjaxResult addCartRelation(String userId, Integer cartId);
    AjaxResult deleteCartRelation(String userId, Integer cartId);
    AjaxResult updateCartRelation(String userId, Integer oldCartId, Integer newCartId);
}
