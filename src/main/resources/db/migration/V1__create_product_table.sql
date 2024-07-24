CREATE TABLE product (
    id UUID PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR NOT NULL,
    price_eur NUMERIC(10, 2) NOT NULL CHECK (price_eur >= 0),
    price_usd NUMERIC(10, 2) NOT NULL CHECK (price_usd >= 0),
    is_available BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
