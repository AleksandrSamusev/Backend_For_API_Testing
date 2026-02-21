package dev.practice.shopapp.mappers;

import dev.practice.shopapp.dto.ProductCreateRequest;
import dev.practice.shopapp.models.Product;

import java.time.LocalDateTime;

public class ProductMapper {
    public static Product toProduct(ProductCreateRequest dto) {
        if(dto == null) {
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
}
