ALTER TABLE order_line_item
    ADD menu_name VARCHAR(255) NOT NULL DEFAULT '';

ALTER TABLE order_line_item
    ADD menu_price DECIMAL(19, 2) NOT NULL DEFAULT 0;
