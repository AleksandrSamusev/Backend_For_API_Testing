package dev.practice.shopapp.repositories.impl.json;

import dev.practice.shopapp.models.Product;
import dev.practice.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import java.nio.file.Files;

import java.io.File;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
@RequiredArgsConstructor
public class JsonProductRepository implements ProductRepository {

    private final ObjectMapper objectMapper;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    @Value("${app.storage.products-path}")
    private String filePath;

    @Override
    public List<Product> findAll() {
        readLock.lock();
        try {
            return readDataFromFile();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Product save(Product product) {
        writeLock.lock();
        try {
            // 1. Get current data using the internal method (no nested locking)
            List<Product> products = readDataFromFile();

            // 2. Add or Update logic
            products.removeIf(p -> p.getId().equals(product.getId()));
            products.add(product);

            // 3. Atomic Write: Save to temp first, then move.
            // This prevents "half-written" files if the app crashes during saving.
            File file = new File(filePath);
            File tempFile = new File(filePath + ".tmp");

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tempFile, products);

            // Standard Java NIO move is more reliable than File.renameTo()
            Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return product;
        } catch (Exception e) {
            throw new RuntimeException("Data persistence error: " + e.getMessage(), e);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Pure logic method: No locking here.
     * It is called by methods that already handle the locking.
     */
    private List<Product> readDataFromFile() {
        try {
            File file = new File(filePath);
            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Product>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON storage: " + e.getMessage(), e);
        }
    }

    public Optional<Product> findById(Long id) {
        readLock.lock();
        try {
            return readDataFromFile().stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst();
        } finally {
            readLock.unlock();
        }
    }
}
