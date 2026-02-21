package dev.practice.shopapp.repositories.impl.json;

import dev.practice.shopapp.repositories.ProductRepository;

public class JsonProductRepository implements ProductRepository {
    private final String filePath = "products.json";
}
