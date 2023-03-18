package com.shoppingstore.ProductService.service;

import com.shoppingstore.ProductService.entity.product.Product;
import com.shoppingstore.ProductService.model.ProductRequest;
import com.shoppingstore.ProductService.model.ProductResponse;

import java.util.List;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
