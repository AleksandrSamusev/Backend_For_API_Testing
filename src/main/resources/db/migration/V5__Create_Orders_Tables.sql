-- 1. Create the 'orders' table
CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        reference_code VARCHAR(50) UNIQUE NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        total_price DECIMAL(8, 2) NOT NULL,
                        currency_code CHAR(3) NOT NULL,
                        tax_amount DECIMAL(8, 2),
                        shipping_cost DECIMAL(8, 2),
                        user_id BIGINT NOT NULL,
                        address_id BIGINT NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP NOT NULL,

    -- Foreign Key constraints for referential integrity
                        CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id),
                        CONSTRAINT fk_order_address FOREIGN KEY (address_id) REFERENCES addresses(id)
);

-- 2. Create the 'order_items' table
CREATE TABLE order_items (
                             id BIGSERIAL PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             product_id BIGINT NOT NULL,
                             product_name VARCHAR(255) NOT NULL,
                             unit_price DECIMAL(8, 2) NOT NULL,
                             quantity INTEGER NOT NULL,
                             total_price DECIMAL(8, 2) NOT NULL,

    -- Foreign Key constraints
                             CONSTRAINT fk_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                             CONSTRAINT fk_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 3. Optimization: Indexes for high-speed telemetry lookups
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_reference ON orders(reference_code);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);