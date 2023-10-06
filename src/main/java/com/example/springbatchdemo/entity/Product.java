package com.example.springbatchdemo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class Product {

    private Integer productId;

    private String productCode;

    private Integer productCost;

    private Integer productDisc;

    private Integer productGst;
}
