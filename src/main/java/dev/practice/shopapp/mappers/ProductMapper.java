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
                .name(safeStrip(dto.getName()))
                .category(safeStrip(dto.getCategory()))
                .manufacturer(safeStrip(dto.getManufacturer()))
                .price(dto.getPrice())
                .costPrice(dto.getCostPrice())
                .salePrice(dto.getSalePrice())
                .currencyCode(safeStrip(dto.getCurrencyCode()))
                .sku(safeStrip(dto.getSku()))
                .quantityInStock(dto.getQuantityInStock())
                .lowStockThreshold(dto.getLowStockThreshold())
                .expectedAvailabilityDate(dto.getExpectedAvailabilityDate())
                .attributes(dto.getAttributes())
                .imageUrl(safeStrip(dto.getImageUrl()))
                .status(dto.getStatus())
                .createdBy(safeStrip(dto.getCreatedBy()))
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
        product.setName(safeStrip(dto.getName()));
        product.setCategory(safeStrip(dto.getCategory()));
        product.setManufacturer(safeStrip(dto.getManufacturer()));
        product.setPrice(dto.getPrice());
        product.setCostPrice(dto.getCostPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setCurrencyCode(safeStrip(dto.getCurrencyCode()));
        product.setLowStockThreshold(dto.getLowStockThreshold());
        product.setImageUrl(safeStrip(dto.getImageUrl()));
    }

    private String safeStrip(String value) {
        return value != null ? value.strip() : null;
    }
}
