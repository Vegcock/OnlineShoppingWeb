package com.web.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Customer {
    private String user_id;      // 用户ID，格式U0001

    private String name;        // 用户姓名
    private String phone;      // 联系电话
    private String email;      // 新增的邮箱字段
    private String address;    // 详细地址
    private String place;      // 地区（省市区）
    private String vip;        // 会员等级
    private Date create_time;   // 注册时间
    private String status;     // 账号状态
    private String url;        // 用户头像URL

    // 如果需要，可以添加枚举类
    public enum VipLevel {
        NORMAL("普通"),
        SILVER("白银"),
        GOLD("黄金"),
        PLATINUM("铂金"),
        DIAMOND("钻石");

        private final String value;
        VipLevel(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    public enum AccountStatus {
        ACTIVATED("已激活"),
        INACTIVE("未激活"),
        BANNED("封禁"),
        DELETED("注销");

        private final String value;
        AccountStatus(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public Date getCreateTime() {
        return create_time;
    }

    public void setCreateTime(Date createTime) {
        this.create_time = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", place='" + place + '\'' +
                ", vip='" + vip + '\'' +
                ", create_time=" + create_time +
                ", status='" + status + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
