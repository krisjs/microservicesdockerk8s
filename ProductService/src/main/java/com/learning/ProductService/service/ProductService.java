package com.learning.ProductService.service;

import com.learning.ProductService.model.ProductRequest;
import com.learning.ProductService.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
