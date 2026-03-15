package dev.practice.shopapp.services.impl;

import dev.practice.shopapp.dto.ProductCreateRequest;
import dev.practice.shopapp.dto.ProductResponse;
import dev.practice.shopapp.dto.ProductUpdateRequest;
import dev.practice.shopapp.exceptions.ResourceNotFoundException;
import dev.practice.shopapp.mappers.ProductMapper;
import dev.practice.shopapp.models.Product;
import dev.practice.shopapp.repositories.ProductRepository;
import dev.practice.shopapp.services.ProductService;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class JpaProductService implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Override
    public ProductResponse createProduct(ProductCreateRequest dto) {

        Product product = mapper.toProduct(dto);
        product.setAttributes(sanitizeAndTrimAttributes(dto.getAttributes()));
        Product savedProduct = productRepository.save(product);
        log.info("Product created with SKU: {}", savedProduct.getSku());
        return mapper.toProductResponse(savedProduct);
    }

    @Override
    public Page<ProductResponse> getProducts(String search, int page, int size, String sort) {
        // 1. Create Pageable (Default to newest first)
        // PRO-TIP: You can expand this to parse 'id,asc' or 'price,desc' later
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Product> productPage;

        // 2. The "Raptor Hunt": Search or Fetch All
        if (search != null && !search.trim().isEmpty()) {
            productPage = productRepository.findBySearchTerm(search.toLowerCase(), pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        // 3. Map to DTOs while preserving the Page metadata (totalPages, totalElements)
        return productPage.map(mapper::toProductResponse);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id)
                .map(mapper::toProductResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public String deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        log.info("[ProductService] SKU Delete: Product with ID {} has been permanently removed.", id);
        return "Product with id: " + id + " permanently deleted";
    }

    @Override
    public Map<String, Object> getInventoryStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSkus", productRepository.count());
        stats.put("lowStockCount", productRepository.countLowStockItems());
        stats.put("totalValue", productRepository.calculateTotalInventoryValue());
        //Get the most expensive "Showcase" item
        productRepository.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "price")))
                .getContent().stream().findFirst()
                .ifPresent(p -> stats.put("topSeller", p.getName()));

        return stats;
    }

    @Override
    public ProductResponse updateProduct(ProductUpdateRequest dto, Long id) {
        Product existing = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product with id: " + id + " not found"));

        // JPA @Version handles the check, but manual check is fine for early exit
        if (!existing.getVersion().equals(dto.getVersion())) {
            throw new OptimisticLockException("Product was updated by another user.");
        }

        // Use the mapper we built with null-checks for partial updates
        mapper.toProductFromUpdateRequest(dto, existing);

        if (dto.getAttributes() != null) {
            existing.setAttributes(sanitizeAndTrimAttributes(dto.getAttributes()));
        }

        // updatedBy is the only audit field we set manually
        existing.setUpdatedBy(dto.getUpdatedBy());

        return mapper.toProductResponse(productRepository.save(existing));
    }




    private Map<String, Object> sanitizeAndTrimAttributes(Map<String, Object> original) {
        if (original == null || original.isEmpty()) return new HashMap<>();

        Map<String, Object> cleanMap = new HashMap<>();

        // Whitelist: Alphanumeric and common punctuation/symbols used in hardware specs
        // This now explicitly includes: % . - ( ) / + " ' and spaces
        String valueRegex = "^[a-zA-Z0-9\\s\\.\\-\\( \\)/\\+\\%\\\"\\']+$";

        original.forEach((key, value) -> {
            String trimmedKey = (key != null) ? key.trim() : "";

            if (value instanceof String str) {
                String trimmedValue = str.trim();
                // If the spec is empty, we allow it, but if it has content, it must match our whitelist
                if (!trimmedValue.isEmpty() && !trimmedValue.matches(valueRegex)) {
                    throw new IllegalArgumentException("Unsafe characters in value for key '" + trimmedKey + "': " + trimmedValue);
                }
                cleanMap.put(trimmedKey, trimmedValue);
            } else {
                cleanMap.put(trimmedKey, value);
            }
        });

        return cleanMap;
    }
}
