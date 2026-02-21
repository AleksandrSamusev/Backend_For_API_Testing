package dev.practice.shopapp.repositories;

import dev.practice.shopapp.models.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
    Product save(Product product);
}
