ALTER TABLE order_table
    ADD created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE order_table
    ADD updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;