package com.web.service.impl;

import com.web.DAO.UserCartDao;
import com.web.entity.AjaxResult;
import com.web.entity.Cart;
import com.web.mapper.CartMapper;
import com.web.service.UserCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCartServiceImpl implements UserCartService {

    private UserCartDao userCartDao = new UserCartDao();

    @Autowired
    private CartMapper cartMapper;

    public List<Cart> getUserCartByUserId(String userId){
        List<Integer> cartIds = userCartDao.findCartIdsByUserId(userId);
        List<Cart> carts = new ArrayList<>(cartIds.size());
        for (Integer cartId : cartIds) {
            carts.add(cartMapper.search(cartId));
        }
        return carts;
    }

    @Override
    public AjaxResult addCartRelation(String userId, Integer cartId) {
        return userCartDao.addCartRelation(userId, cartId);
    }

    @Override
    public AjaxResult deleteCartRelation(String userId, Integer cartId) {
        return userCartDao.deleteCartRelation(userId, cartId);
    }

    @Override
    public AjaxResult updateCartRelation(String userId, Integer oldCartId, Integer newCartId) {
        return userCartDao.updateCartRelation(userId, oldCartId, newCartId);
    }
}
