package com.example.springbatchdemo.processor;

import com.example.springbatchdemo.entity.Product;
import org.springframework.batch.item.ItemProcessor;

public class ProductProcessor implements ItemProcessor<Product,Product> {
    @Override
    public Product process(Product item) throws Exception {
        Integer cost=item.getProductCost();
        item.setProductDisc(cost * 12);
        item.setProductGst(cost * 22);
        return item;
    }
}
