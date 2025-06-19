package com.web.controller;

import com.web.entity.AjaxResult;
import com.web.entity.OnsaleComd;
import com.web.service.OnsaleComdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // 允许跨域
public class OnsaleComdController {
    @Autowired
    private OnsaleComdService onsaleComdService;

    @RequestMapping("/ocmd/list")
    public AjaxResult list(){
        List<OnsaleComd> onsaleComdList = onsaleComdService.list();
        return AjaxResult.success(onsaleComdList);
    }

    @DeleteMapping("/ocmd/{id}")
    public AjaxResult remove(@PathVariable Integer id){
        onsaleComdService.remove(id);
        return AjaxResult.success();
    }

    @PostMapping("/ocmd")
    public AjaxResult add(@RequestBody OnsaleComd onsaleComd){
        onsaleComdService.add(onsaleComd);
        return AjaxResult.success();
    }

    @PutMapping("/ocmd")
    public AjaxResult edit(@RequestBody OnsaleComd onsaleComd){
        onsaleComdService.edit(onsaleComd);
        return AjaxResult.success();
    }

    @GetMapping("/ocmd/{id}")
    public AjaxResult search(@PathVariable Integer id){
        OnsaleComd OnsaleComd = onsaleComdService.search(id);
        return AjaxResult.success(OnsaleComd);
    }
}
