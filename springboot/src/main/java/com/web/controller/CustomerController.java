package com.web.controller;

import com.web.entity.AjaxResult;
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

    @RequestMapping("/ct/list")
    public AjaxResult list(){
        List<Customer> customerList = customerService.list();
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
