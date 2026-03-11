package dev.practice.shopapp.repositories;


import dev.practice.shopapp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Allows you to find a product by its unique SKU (useful for updates/checks)
    Optional<Product> findBySku(String sku);
}
