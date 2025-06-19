package com.web.service;

import com.web.entity.AjaxResult;
import com.web.entity.OnsaleComd;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OnsaleComdService {

    AjaxResult add(OnsaleComd onsaleComd);
    AjaxResult remove(Integer id);
    AjaxResult removeByComd(Integer id);
    AjaxResult edit(OnsaleComd onsaleComd);
    List<OnsaleComd> list();
    OnsaleComd search(Integer id);
}
