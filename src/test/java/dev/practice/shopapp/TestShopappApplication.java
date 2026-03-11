package dev.practice.shopapp;

import org.springframework.boot.SpringApplication;

public class TestShopappApplication {
    public static void main(String[] args) {
        SpringApplication.from(ShopappApplication::main)
                .with(TestcontainersConfiguration.class, DataSeeder.class)
                .run(args);
    }
}
