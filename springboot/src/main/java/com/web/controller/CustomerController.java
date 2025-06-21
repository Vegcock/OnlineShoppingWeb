package com.web.controller;

import com.web.Utils.RedisUtil;
import com.web.entity.AjaxResult;
import com.web.entity.Commodity;
import com.web.entity.Customer;
import com.web.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // 允许跨域
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/ct/list")
    public AjaxResult list(){
        String cacheKey = "customer:list:all";

        if (redisUtil.hasKey(cacheKey)) {
            List<Customer> cachedList = (List<Customer>) redisUtil.get(cacheKey);
            return AjaxResult.success(cachedList);
        }

        List<Customer> customerList = customerService.list();
        redisUtil.set(cacheKey, customerList, 10); // 缓存10分钟
        return AjaxResult.success(customerList);
    }

    @DeleteMapping("/ct/{id}")
    public AjaxResult remove(@PathVariable String id){
        return customerService.remove(id);
    }

    @PostMapping("/ct")
    public AjaxResult add(@RequestBody Customer customer){
        return customerService.add(customer);
    }

    @PutMapping("/ct")
    public AjaxResult edit(@RequestBody Customer customer){
        return customerService.edit(customer);
    }

    @GetMapping("/ct/i/{id}")
    public AjaxResult searchById(@PathVariable String id){
        Customer customer = customerService.selectById(id);
        return AjaxResult.success(customer);
    }

    @GetMapping("/ct/p/{phone}")
    public AjaxResult searchByPhone(@PathVariable String phone){
        Customer customer = customerService.selectByPhone(phone);
        return AjaxResult.success(customer);
    }

    @GetMapping("/ct/e/{email}")
    public AjaxResult searchByEmail(@PathVariable String email){
        Customer customer = customerService.selectByEmail(email);
        return AjaxResult.success(customer);
    }
}
