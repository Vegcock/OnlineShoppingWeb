package com.web.service;

import com.web.entity.AjaxResult;
import com.web.entity.Commodity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommodityService {
    AjaxResult add(Commodity commodity);
    AjaxResult remove(Integer id);
    AjaxResult edit(Commodity commodity);
    List<Commodity> list();
    Commodity search(Integer id);
}
