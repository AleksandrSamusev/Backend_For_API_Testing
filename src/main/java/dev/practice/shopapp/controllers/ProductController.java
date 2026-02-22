package dev.practice.shopapp.controllers;

import dev.practice.shopapp.dto.ProductCreateRequest;
import dev.practice.shopapp.dto.ProductResponse;
import dev.practice.shopapp.dto.ProductUpdateRequest;
import dev.practice.shopapp.models.ApiResponse;
import dev.practice.shopapp.models.Product;
import dev.practice.shopapp.services.ProductService;
import dev.practice.shopapp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody ProductCreateRequest dto,
                                                       HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(productService.createProduct(dto),
                        "Product successfully created",
                        request.getRequestURI()), HttpStatus.CREATED
        );
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts(HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(productService.getProducts(),
                        "Success",
                        request.getRequestURI()), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id,
                                                        HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(productService.getProductById(id),
                        "Success",
                        request.getRequestURI()), HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id,
                                                   HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(productService.deleteProductById(id),
                        "Success",
                        request.getRequestURI()), HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@Valid @RequestBody ProductUpdateRequest dto,
                                                               @PathVariable Long id,
                                                               HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(productService.updateProduct(dto, id),
                        "Success",
                        request.getRequestURI()), HttpStatus.OK
        );
    }
}
