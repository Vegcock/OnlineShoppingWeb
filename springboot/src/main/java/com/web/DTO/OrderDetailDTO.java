package com.web.DTO;

import com.web.entity.Customer;
import com.web.entity.Delivery;
import com.web.entity.Order;

public class OrderDetailDTO {
    private Order order;
    private Customer customer;
    private Delivery delivery;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}
