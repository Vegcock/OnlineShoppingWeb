package com.web.service.impl;

import com.web.entity.AjaxResult;
import com.web.entity.Customer;
import com.web.mapper.CustomerMapper;
import com.web.service.CustomerService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public AjaxResult add(Customer customer) {

        if(existEmail(customer)){
            return AjaxResult.error("该邮箱已被注册");
        }

            if(existPhone(customer)){
            return AjaxResult.error("该号码已被注册");
        }

        if (StringUtils.isBlank(customer.getVip())) {
            customer.setVip(Customer.VipLevel.NORMAL.getValue());
        }
        if (StringUtils.isBlank(customer.getStatus())) {
            customer.setStatus(Customer.AccountStatus.INACTIVE.getValue());
        }
        if (StringUtils.isBlank(customer.getUrl())) {
            customer.setUrl("/assets/default-avatar.png");
        }

        // 生成用户ID
        customer.setUserId(generateUserId());

        int result = customerMapper.insert(customer);
        return result > 0 ? AjaxResult.success("注册成功") : AjaxResult.error("注册失败");
    }

    @Override
    public AjaxResult remove(String id) {
        int result = customerMapper.deleteById(id);
        return result > 0 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    @Override
    public AjaxResult edit(Customer customer) {
        if(existEmail(customer)){
            return AjaxResult.error("该邮箱已被注册");
        }
        if(existPhone(customer)){
            return AjaxResult.error("该号码已被注册");
        }
        int result = customerMapper.update(customer);
        return result > 0 ? AjaxResult.success("更新成功") : AjaxResult.error("更新失败");
    }

    @Override
    public List<Customer> list() {
        List<Customer> customers =  customerMapper.list();
        return customers;
    }

    @Override
    public Customer selectById(String id) {
        return customerMapper.selectById(id);
    }

    @Override
    public Customer selectByEmail(String email) {
        return customerMapper.selectByEmail(email);
    }

    @Override
    public Customer selectByPhone(String phone) {
        return customerMapper.selectByPhone(phone);
    }

    private String generateUserId() {
        // 实现ID生成逻辑，如U0001格式
        // 可以使用之前提到的存储函数或序列
        return "U" + String.format("%04d", getNextUserIdSequence());
    }

    private int getNextUserIdSequence() {
        // 实现获取下一个ID序列的逻辑
        // 可以从数据库序列或MAX(user_id)+1获取
        return (int) (Math.random() * 9000 + 1);
    }

    private boolean existEmail(Customer customer) {
        if (StringUtils.isNotBlank(customer.getEmail())) {
            Customer existing = customerMapper.selectByEmail(customer.getEmail());
            // 如果查到了，并且不是当前用户自己的记录，才算“重复”
            if (existing != null && !existing.getUserId().equals(customer.getUserId())) {
                return true;
            }
        }
        return false;
    }

    private boolean existPhone(Customer customer) {
        if (StringUtils.isNotBlank(customer.getPhone())) {
            Customer existing = customerMapper.selectByPhone(customer.getPhone());
            if (existing != null && !existing.getUserId().equals(customer.getUserId())) {
                return true;
            }
        }
        return false;
    }
}
