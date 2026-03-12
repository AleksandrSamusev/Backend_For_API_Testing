package dev.practice.shopapp.repositories;


import dev.practice.shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Allows you to find a product by its unique SKU (useful for updates/checks)
    Optional<Product> findBySku(String sku);
    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.name) LIKE %:term% OR " +
            "LOWER(p.sku) LIKE %:term% OR " +
            "LOWER(p.category) LIKE %:term%")
    Page<Product> findBySearchTerm(String term, Pageable pageable);
}
