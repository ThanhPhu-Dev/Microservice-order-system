CREATE TABLE orders (
    id INT PRIMARY KEY,
    user_id VARCHAR(20),
    total_price DECIMAL(10,2),
    created_at TIMESTAMP
);