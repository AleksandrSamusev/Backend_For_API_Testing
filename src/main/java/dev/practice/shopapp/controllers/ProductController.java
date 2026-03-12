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

import java.util.List;

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
            // 1. Search across Name, SKU, Category
            @RequestParam(required = false) String search,
            // 2. Pagination controls
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            // 3. Sorting (Matching your Veloce UI sorting toggle)
            @RequestParam(defaultValue = "id,desc") String sort) {

        // Return the Page object containing metadata like totalElements and totalPages
        return new ResponseEntity<>(
                ResponseUtil.success(
                        productService.getProducts(search, page, size, sort),
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
}
