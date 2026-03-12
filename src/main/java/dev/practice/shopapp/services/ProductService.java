package dev.practice.shopapp.services;

import dev.practice.shopapp.dto.ProductCreateRequest;
import dev.practice.shopapp.dto.ProductResponse;
import dev.practice.shopapp.dto.ProductUpdateRequest;
import org.springframework.data.domain.Page;

// 1. You can remove the 'Product' and 'List' imports if they aren't used elsewhere
public interface ProductService {

    // FIX: Return ProductResponse for perfect API consistency
    ProductResponse createProduct(ProductCreateRequest dto);

    Page<ProductResponse> getProducts(String search, int page, int size, String sort);

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(ProductUpdateRequest dto, Long id);

    String deleteProductById(Long id);
}
