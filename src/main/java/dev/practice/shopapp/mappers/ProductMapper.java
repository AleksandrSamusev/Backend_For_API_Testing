package dev.practice.shopapp.mappers;

import dev.practice.shopapp.dto.ProductCreateRequest;
import dev.practice.shopapp.dto.ProductResponse;
import dev.practice.shopapp.dto.ProductUpdateRequest;
import dev.practice.shopapp.enums.AvailabilityStatus;
import dev.practice.shopapp.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toProduct(ProductCreateRequest dto) {
        if (dto == null) return null;

        Product product = new Product();

        // 1. Standard Fields
        product.setName(safeStrip(dto.getName()));
        product.setCategory(safeStrip(dto.getCategory()));
        product.setManufacturer(safeStrip(dto.getManufacturer()));
        product.setPrice(dto.getPrice());
        product.setCostPrice(dto.getCostPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setCurrencyCode(safeStrip(dto.getCurrencyCode()));
        product.setSku(safeStrip(dto.getSku()));
        product.setLowStockThreshold(dto.getLowStockThreshold());
        product.setImageUrl(safeStrip(dto.getImageUrl()));
        product.setAttributes(dto.getAttributes());
        product.setCreatedBy(safeStrip(dto.getCreatedBy()));
        product.setUpdatedBy(safeStrip(dto.getCreatedBy()));

        // 2. TRIGGER SMART LOGIC (The "Raptor" Move)
        // By setting quantity here, the Entity's setQuantityInStock()
        // automatically determines if status is IN_STOCK or OUT_OF_STOCK.
        product.setQuantityInStock(dto.getQuantityInStock());

        // 3. Optional Date for future Preorders
        product.setExpectedAvailabilityDate(dto.getExpectedAvailabilityDate());

        return product;
    }

    public void toProductFromUpdateRequest(ProductUpdateRequest dto, Product product) {
        if (dto == null || product == null) return;

        // 1. Strings
        if (dto.getName() != null) product.setName(safeStrip(dto.getName()));
        if (dto.getCategory() != null) product.setCategory(safeStrip(dto.getCategory()));
        if (dto.getManufacturer() != null) product.setManufacturer(safeStrip(dto.getManufacturer()));
        if (dto.getCurrencyCode() != null) product.setCurrencyCode(safeStrip(dto.getCurrencyCode()));
        if (dto.getImageUrl() != null) product.setImageUrl(safeStrip(dto.getImageUrl()));

        // 2. Financials
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getCostPrice() != null) product.setCostPrice(dto.getCostPrice());
        if (dto.getSalePrice() != null) product.setSalePrice(dto.getSalePrice());

        // 3. Stock - This triggers your "Human Factor Insurance" automatically
        if (dto.getQuantityInStock() != null) {
            product.setQuantityInStock(dto.getQuantityInStock());
        }
        if (dto.getLowStockThreshold() != null) {
            product.setLowStockThreshold(dto.getLowStockThreshold());
        }

        // 4. Status - Handling based on your specific Entity methods
        if (dto.getStatus() != null) {
            AvailabilityStatus status = dto.getStatus();
            switch (status) {
                case PREORDER -> product.startPreorder(dto.getExpectedAvailabilityDate());
                case BACKORDER -> product.startBackorder(dto.getExpectedAvailabilityDate());
                default -> product.setQuantityInStock(product.getQuantityInStock()); // Refresh status logic
            }
        }

        // 5. Cleanup
        if (dto.getExpectedAvailabilityDate() != null) {
            product.setExpectedAvailabilityDate(dto.getExpectedAvailabilityDate());
        }
        if (dto.getAttributes() != null) product.setAttributes(dto.getAttributes());
        if (dto.getUpdatedBy() != null) product.setUpdatedBy(safeStrip(dto.getUpdatedBy()));
    }

    public ProductResponse toProductResponse(Product product) {
        if (product == null) return null;
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

    private String safeStrip(String value) {
        return value != null ? value.strip() : null;
    }
}