CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    isbn VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    unit_price NUMERIC(12,2) NOT NULL,
    total_amount NUMERIC(12,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_orders_user ON orders (user_id);
CREATE INDEX IF NOT EXISTS idx_orders_isbn ON orders (isbn);

CREATE TABLE IF NOT EXISTS idempotency_keys (
    id UUID PRIMARY KEY,
    idem_key VARCHAR(100) UNIQUE NOT NULL,
    order_id UUID UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);