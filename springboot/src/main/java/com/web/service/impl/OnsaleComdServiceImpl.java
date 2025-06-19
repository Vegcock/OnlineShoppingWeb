package com.web.service.impl;

import com.web.entity.AjaxResult;
import com.web.entity.OnsaleComd;
import com.web.mapper.OnsaleComdMapper;
import com.web.service.OnsaleComdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OnsaleComdServiceImpl implements OnsaleComdService {
    @Autowired
    private OnsaleComdMapper onsaleComdMapper;

    @Override
    public AjaxResult add(OnsaleComd OnsaleComd) {
        onsaleComdMapper.add(OnsaleComd);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult remove(Integer id) {
        onsaleComdMapper.remove(id);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult removeByComd(Integer id) {
        onsaleComdMapper.removeByComd(id);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult edit(OnsaleComd OnsaleComd) {
        onsaleComdMapper.edit(OnsaleComd);
        return AjaxResult.success();
    }

    @Override
    public List<OnsaleComd> list() {
        return onsaleComdMapper.list();
    }

    @Override
    public OnsaleComd search(Integer id) {
        return onsaleComdMapper.search(id);
    }
}
