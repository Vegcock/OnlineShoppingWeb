package com.web.service.impl;

import com.web.entity.AjaxResult;
import com.web.entity.Commodity;
import com.web.entity.OnsaleComd;
import com.web.mapper.CommodityMapper;
import com.web.mapper.OnsaleComdMapper;
import com.web.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class CommodityServiceImpl implements CommodityService {
    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private OnsaleComdMapper onsaleComdMapper;

    @Override
    public AjaxResult add(Commodity commodity) {
        commodityMapper.add(commodity);
        if("上架".equals(commodity.getStatus())){
            int randomNum = new Random().nextInt(8) + 1; // 1-8
            OnsaleComd onsaleComd = new OnsaleComd(commodity.getName(),
                    commodity.getDescription(),commodity.getStatus(),commodity.getPrice(),"/assets/" + randomNum + ".png",commodity.getId());
            onsaleComdMapper.add(onsaleComd);
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult remove(Integer id) {
        commodityMapper.remove(id);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult edit(Commodity commodity) {
        commodityMapper.edit(commodity);
        if("下架".equals(commodity.getStatus())){
            onsaleComdMapper.removeByComd(commodity.getId());
        }
        return AjaxResult.success();
    }

    @Override
    public List<Commodity> list() {
        return commodityMapper.list();
    }

    @Override
    public Commodity search(Integer id) {
        return commodityMapper.search(id);
    }
}
