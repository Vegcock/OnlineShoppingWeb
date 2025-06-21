package com.web.service.impl;

import com.web.DAO.UserCartDao;
import com.web.entity.AjaxResult;
import com.web.entity.Cart;
import com.web.mapper.CartMapper;
import com.web.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private UserCartDao userCartDao = new UserCartDao();

    @Autowired
    private CartMapper cartMapper;
    
    @Override
    public AjaxResult add(Cart cart) {
        cartMapper.add(cart);
        Integer insertedId = cart.getId();
        return AjaxResult.success(insertedId);
    }

    @Override
    public AjaxResult remove(Integer id) {
        cartMapper.remove(id);
        userCartDao.deleteCartOnly(id);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult deleteBatchByIds(List<Integer> ids) {
        for(Integer id : ids){
            cartMapper.remove(id);
            userCartDao.deleteCartOnly(id);
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult edit(Cart cart) {
        cartMapper.edit(cart);
        return AjaxResult.success();
    }

    @Override
    public List<Cart> list() {
        return cartMapper.list();
    }

    @Override
    public Cart search(Integer id) {
        return cartMapper.search(id);
    }
}
