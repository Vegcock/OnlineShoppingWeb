package com.web.controller;

import com.web.Utils.RedisUtil;
import com.web.entity.AjaxResult;
import com.web.entity.Commodity;
import com.web.service.CommodityService;
import com.web.service.OnsaleComdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // 允许跨域
public class CommodityController {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private OnsaleComdService onsaleComdService;

    @RequestMapping("/cmd/list")
    public AjaxResult list(){
        String cacheKey = "commodity:list";

        // 优先查 Redis 缓存
        if (redisUtil.hasKey(cacheKey)) {
            List<Commodity> cachedList = (List<Commodity>) redisUtil.get(cacheKey);
            return AjaxResult.success(cachedList);
        }

        // 缓存不存在，查数据库并缓存
        List<Commodity> commodityList = commodityService.list();
        redisUtil.set(cacheKey, commodityList, 10); // 缓存10分钟
        return AjaxResult.success(commodityList);
    }

    @DeleteMapping("/cmd/{id}")
    public AjaxResult remove(@PathVariable Integer id){
        commodityService.remove(id);
        onsaleComdService.removeByComd(id);
        return AjaxResult.success();
    }

    @PostMapping("/cmd")
    public AjaxResult add(@RequestBody Commodity commodity){
        commodityService.add(commodity);
        return AjaxResult.success();
    }

    @PutMapping("/cmd")
    public AjaxResult edit(@RequestBody Commodity commodity){
        commodityService.edit(commodity);
        return AjaxResult.success();
    }

    @GetMapping("/cmd/{id}")
    public AjaxResult search(@PathVariable Integer id){
        Commodity commodity = commodityService.search(id);
        return AjaxResult.success(commodity);
    }
}
