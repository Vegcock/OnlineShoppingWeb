package com.web.controller;

import com.web.entity.AjaxResult;
import com.web.entity.Favorite;
import com.web.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
@CrossOrigin(origins = "*") // 允许跨域
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/add")
    public AjaxResult add(@RequestParam String userId, @RequestParam Integer commodityId) {
        favoriteService.add(userId, commodityId);
        return AjaxResult.success("添加收藏成功");
    }

    @PostMapping("/delete")
    public AjaxResult delete(@RequestParam String userId, @RequestParam Integer commodityId) {
        favoriteService.delete(userId, commodityId);
        return AjaxResult.success("取消收藏成功");
    }

    @GetMapping("/check")
    public AjaxResult check(@RequestParam String userId, @RequestParam Integer commodityId) {
        boolean exists = favoriteService.isFavorited(userId, commodityId);
        return AjaxResult.success(exists);
    }

    @GetMapping("/list")
    public AjaxResult list(@RequestParam String userId) {
        return AjaxResult.success(favoriteService.getByUser(userId));
    }
}
