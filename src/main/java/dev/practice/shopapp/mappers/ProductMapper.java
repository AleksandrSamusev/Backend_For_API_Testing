package dev.practice.shopapp.mappers;

import dev.practice.shopapp.dto.ProductCreateRequest;
import dev.practice.shopapp.dto.ProductResponse;
import dev.practice.shopapp.dto.ProductUpdateRequest;
import dev.practice.shopapp.models.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductMapper {
    public Product toProduct(ProductCreateRequest dto) {
        if (dto == null) {
            return null;
        }
        return Product.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .manufacturer(dto.getManufacturer())
                .price(dto.getPrice())
                .costPrice(dto.getCostPrice())
                .salePrice(dto.getSalePrice())
                .currencyCode(dto.getCurrencyCode())
                .sku(dto.getSku())
                .quantityInStock(dto.getQuantityInStock())
                .lowStockThreshold(dto.getLowStockThreshold())
                .expectedAvailabilityDate(dto.getExpectedAvailabilityDate())
                .attributes(dto.getAttributes())
                .imageUrl(dto.getImageUrl())
                .status(dto.getStatus())
                .createdBy(dto.getCreatedBy())
                .createdAt(LocalDateTime.now())
                .version(0L)
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .manufacturer(product.getManufacturer())
                .price(product.getPrice())
                .salePrice(product.getSalePrice())
                .currencyCode(product.getCurrencyCode())
                .sku(product.getSku())
                .quantityInStock(product.getQuantityInStock())
                .expectedAvailabilityDate(product.getExpectedAvailabilityDate())
                .attributes(product.getAttributes())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus())
                .version(product.getVersion())
                .build();
    }

    public void toProductFromUpdateRequest(ProductUpdateRequest dto, Product product) {
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setManufacturer(dto.getManufacturer());
        product.setPrice(dto.getPrice());
        product.setCostPrice(dto.getCostPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setCurrencyCode(dto.getCurrencyCode());
        product.setLowStockThreshold(dto.getLowStockThreshold());
        product.setImageUrl(dto.getImageUrl());
    }
}
