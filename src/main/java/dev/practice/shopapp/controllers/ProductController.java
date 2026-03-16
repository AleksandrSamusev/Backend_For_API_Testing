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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductCreateRequest dto,
                                                       HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(productService.createProduct(dto),
                        "Product successfully created",
                        request.getRequestURI()), HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProducts(
            HttpServletRequest request,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        Page<ProductResponse> products = productService.getFilteredProducts(
                category, minPrice, maxPrice, search, page, size, sort
        );
        return new ResponseEntity<>(
                ResponseUtil.success(
                        products,
                        "Products retrieved successfully",
                        request.getRequestURI()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id,
                                                        HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(productService.getProductById(id),
                        "Success",
                        request.getRequestURI()), HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id,
                                                             HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(productService.deleteProductById(id),
                        "Success",
                        request.getRequestURI()), HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@Valid @RequestBody ProductUpdateRequest dto,
                                                               @PathVariable Long id,
                                                               HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(productService.updateProduct(dto, id),
                        "Success",
                        request.getRequestURI()), HttpStatus.OK
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInventoryStats(HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(
                        productService.getInventoryStats(),
                        "Inventory metrics retrieved",
                        request.getRequestURI()
                ),
                HttpStatus.OK
        );
    }
}
