package com.web.service;

import com.web.entity.AjaxResult;
import com.web.entity.Cart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    AjaxResult add(Cart cart);
    AjaxResult remove(Integer id);
    AjaxResult edit(Cart cart);
    List<Cart> list();
    Cart search(Integer id);
    AjaxResult deleteBatchByIds(List<Integer> ids);
}
