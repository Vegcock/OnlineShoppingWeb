package com.web.controller;

import com.web.Utils.RedisUtil;
import com.web.entity.AjaxResult;
import com.web.entity.Commodity;
import com.web.service.CommodityService;
import com.web.service.OnsaleComdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // 允许跨域
public class CommodityController {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private OnsaleComdService onsaleComdService;

    @RequestMapping("/cmd/all")
    public AjaxResult list(@RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "10") int pageSize) {
        String cacheKey = "commodity:list";

        List<Commodity> commodityList;

        // 先查缓存
        if (redisUtil.hasKey(cacheKey)) {
            commodityList = (List<Commodity>) redisUtil.get(cacheKey);
        } else {
            // 缓存不存在，查数据库并缓存
            commodityList = commodityService.list();
            redisUtil.set(cacheKey, commodityList, 10); // 缓存10分钟
        }

        // 手动分页（逻辑分页）
        int total = commodityList.size();
        int fromIndex = Math.min((pageNum - 1) * pageSize, total);
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<Commodity> pageList = commodityList.subList(fromIndex, toIndex);

        // 构造分页信息（模仿 PageInfo）
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("list", pageList);

        return AjaxResult.success(result);
    }

    @RequestMapping("/cmd/list")
    public AjaxResult listAll() {
        String cacheKey = "commodity:list:all";

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
