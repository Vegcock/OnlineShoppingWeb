package com.web.entity;

import lombok.Data;

@Data
public class OnsaleComd {
    private Integer id;
    private String name;
    private String description;
    private String status;
    private float price;
    private String url;
    private Integer comdId;

    public OnsaleComd() {
    }

    public OnsaleComd(String name, String description, String status, float price, String url, Integer comdId) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.price = price;
        this.url = url;
        this.comdId = comdId;
    }

    public Integer getComdId() {
        return comdId;
    }

    public void setComdId(Integer comdId) {
        this.comdId = comdId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
