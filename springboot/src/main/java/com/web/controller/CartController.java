package com.web.controller;

import com.web.entity.AjaxResult;
import com.web.entity.Cart;
import com.web.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*") // 允许跨域
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("/cr/list")
    public AjaxResult list(){
        List<Cart> cartList = cartService.list();
        return AjaxResult.success(cartList);
    }

    @DeleteMapping("/cr/{id}")
    public AjaxResult remove(@PathVariable Integer id){
        return cartService.remove(id);
    }

    @DeleteMapping("/cr")
    public AjaxResult deleteBatch(@RequestParam("ids") String ids) {
        List<Integer> idList = Arrays.stream(ids.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        cartService.deleteBatchByIds(idList);
        return AjaxResult.success("删除成功");
    }

    @PostMapping("/cr")
    public AjaxResult add(@RequestBody Cart cart){
        return cartService.add(cart);
    }

    @PutMapping("/cr")
    public AjaxResult edit(@RequestBody Cart cart){
        return cartService.edit(cart);
    }

    @GetMapping("/cr/{id}")
    public AjaxResult search(@PathVariable Integer id){
        Cart cart = cartService.search(id);
        return AjaxResult.success(cart);
    }
}
