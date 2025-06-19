package com.web.service;

import com.web.entity.AjaxResult;
import com.web.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    AjaxResult add(Customer customer);
    AjaxResult remove(String id);
    AjaxResult edit(Customer customer);
    List<Customer> list();
    Customer selectById(String id);
    Customer selectByEmail(String email);
    Customer selectByPhone(String phone);
}
