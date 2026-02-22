package dev.practice.shopapp.services.impl;

import dev.practice.shopapp.dto.ProductCreateRequest;
import dev.practice.shopapp.dto.ProductResponse;
import dev.practice.shopapp.dto.ProductUpdateRequest;
import dev.practice.shopapp.enums.AvailabilityStatus;
import dev.practice.shopapp.exceptions.ResourceNotFoundException;
import dev.practice.shopapp.mappers.ProductMapper;
import dev.practice.shopapp.models.Product;
import dev.practice.shopapp.repositories.ProductRepository;
import dev.practice.shopapp.services.ProductService;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Override
    public Product createProduct(ProductCreateRequest dto) {
        // Create Product and generate id
        Map<String, Object> cleanAttributes = sanitizeAndTrimAttributes(dto.getAttributes());
        Product product = mapper.toProduct(dto);
        product.setAttributes(cleanAttributes);
        product.setId(System.currentTimeMillis());
        product.updateStock(dto.getQuantityInStock());
        return productRepository.save(product);
    }

    @Override
    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream()
                .filter(i -> i.getStatus() != AvailabilityStatus.ARCHIVED)
                .map(mapper::toProductResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return productRepository.findAll().stream()
                .filter(i -> Objects.equals(i.getId(), id))
                .filter(i -> i.getStatus() != AvailabilityStatus.ARCHIVED)
                .findFirst()
                .map(mapper::toProductResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with given id: " + id));
    }

    @Override
    public String deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with given id: " + id));
        productRepository.save(product.deleteProduct("SYSTEM"));
        return "Product with id: " + id + " successfully deleted";
    }

    @Override
    public ProductResponse updateProduct(ProductUpdateRequest dto, Long id) {
        Product existing = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product with id: " + id + " not found"));

        if (!existing.getVersion().equals(dto.getVersion())) {
            throw new OptimisticLockException("Product was updated by another user. Please refresh.");
        }
        mapper.toProductFromUpdateRequest(dto, existing);
        existing.setAttributes(sanitizeAndTrimAttributes(dto.getAttributes()));
        existing.updateStock(dto.getQuantityInStock());
        if(dto.getStatus()==AvailabilityStatus.BACKORDER) {
            existing.startBackorder(dto.getExpectedAvailabilityDate());
        } else if (dto.getStatus()==AvailabilityStatus.PREORDER) {
            existing.startPreorder(dto.getExpectedAvailabilityDate());
        }
        existing.setVersion(existing.getVersion() + 1);
        existing.setUpdatedBy("SYSTEM");
        existing.setUpdatedAt(LocalDateTime.now());
        return mapper.toProductResponse(productRepository.save(existing));
    }

    private Map<String, Object> sanitizeAndTrimAttributes(Map<String, Object> original) {
        if (original == null || original.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Object> cleanMap = new HashMap<>();

        // Regex: Keys (Alphanumeric/Underscore), Values (Safe characters)
        String keyRegex = "^[a-zA-Z0-9_]+$";
        String valueRegex = "^[\\p{L}\\p{N}\\s\\-_.]+$";

        original.forEach((key, value) -> {
            // 1. Trim and Validate Key
            String trimmedKey = (key != null) ? key.trim() : "";

            if (trimmedKey.isEmpty()) {
                throw new IllegalArgumentException("Attribute key cannot be empty");
            }
            if (!trimmedKey.matches(keyRegex)) {
                throw new IllegalArgumentException("Invalid characters in attribute key: " + trimmedKey);
            }

            // 2. Trim and Validate Value
            Object processedValue = value;

            if (value instanceof String str) {
                String trimmedValue = str.trim();
                if (!trimmedValue.matches(valueRegex)) {
                    throw new IllegalArgumentException("Unsafe characters in value for key '" + trimmedKey + "': " + trimmedValue);
                }
                processedValue = trimmedValue;

            } else if (value instanceof List<?> list) {
                // Handle List of Strings (common for attributes like 'colors')
                processedValue = list.stream()
                        .map(item -> {
                            if (item instanceof String s) {
                                String ts = s.trim();
                                if (!ts.matches(valueRegex))
                                    throw new IllegalArgumentException("Unsafe string in list: " + ts);
                                return ts;
                            }
                            return item;
                        })
                        .toList();
            }

            // 3. Put into the NEW map
            cleanMap.put(trimmedKey, processedValue);
        });

        return cleanMap;
    }
}
