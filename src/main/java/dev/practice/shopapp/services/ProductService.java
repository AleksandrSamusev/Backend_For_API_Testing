package dev.practice.shopapp.services;

import dev.practice.shopapp.dto.ProductCreateRequest;
import dev.practice.shopapp.dto.ProductResponse;
import dev.practice.shopapp.dto.ProductUpdateRequest;
import dev.practice.shopapp.models.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(ProductCreateRequest dto);

    List<ProductResponse> getProducts();

    ProductResponse getProductById(Long id);

    String deleteProductById(Long id);

    ProductResponse updateProduct(ProductUpdateRequest dto, Long id);
}
