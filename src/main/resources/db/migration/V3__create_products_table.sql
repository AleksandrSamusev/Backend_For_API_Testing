
CREATE TABLE IF NOT EXISTS products (
                          id BIGSERIAL PRIMARY KEY,

    -- 1. Identity & Classification
                          sku VARCHAR(30) NOT NULL UNIQUE,
                          name VARCHAR(150) NOT NULL,
                          category VARCHAR(50) NOT NULL,
                          manufacturer VARCHAR(150) NOT NULL,

    -- 2. Financial Precision (BigDecimal maps to NUMERIC)
                          price NUMERIC(8, 2) NOT NULL,
                          cost_price NUMERIC(8, 2) NOT NULL,
                          sale_price NUMERIC(8, 2),
                          currency_code CHAR(3) NOT NULL DEFAULT 'USD',

    -- 3. Inventory & Status Logic
                          quantity_in_stock INTEGER NOT NULL DEFAULT 0,
                          low_stock_threshold INTEGER NOT NULL DEFAULT 10,
                          status VARCHAR(20) NOT NULL DEFAULT 'OUT_OF_STOCK',
                          expected_availability_date TIMESTAMP,

    -- 4. High-Density Metadata
                          attributes JSONB DEFAULT '{}',
                          image_url TEXT,

    -- 5. Enterprise Audit Trail
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          created_by VARCHAR(50) NOT NULL,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_by VARCHAR(50),
                          version BIGINT NOT NULL DEFAULT 0
);

-- PRO-TIP: Indexes for the Search
CREATE INDEX IF NOT EXISTS idx_products_sku ON products(sku);
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);
CREATE INDEX IF NOT EXISTS idx_products_attributes ON products USING GIN (attributes);