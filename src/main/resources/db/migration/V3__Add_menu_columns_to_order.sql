ALTER TABLE order_line_item
    DROP menu_id;

ALTER TABLE order_line_item
    ADD menu_name VARCHAR(255) not null;

ALTER TABLE order_line_item
    ADD menu_price DECIMAL(19, 2) NOT NULL;
