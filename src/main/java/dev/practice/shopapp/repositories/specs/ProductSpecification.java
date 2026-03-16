package dev.practice.shopapp.repositories.specs;

import dev.practice.shopapp.models.Product;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<Product> filterBy(String category, BigDecimal min, BigDecimal max, String search) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 🚀 CATEGORY FILTER: Exact match
            if (category != null && !category.isEmpty()) {
                predicates.add(cb.equal(root.get("category"), category));
            }

            // 🚀 PRICE RANGE: Greater Than / Less Than
            if (min != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), min));
            }
            if (max != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), max));
            }

            // 🚀 GLOBAL SEARCH: Name OR SKU OR Manufacturer
            if (search != null && !search.isEmpty()) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("sku")), pattern),
                        cb.like(cb.lower(root.get("manufacturer")), pattern),
                        cb.like(cb.lower(root.get("category")), pattern) // 🚀 THE ADDITION
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
