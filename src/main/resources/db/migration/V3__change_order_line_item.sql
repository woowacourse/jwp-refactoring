ALTER TABLE order_line_item
    ADD menu_name VARCHAR(255) NOT NULL;
ALTER TABLE order_line_item
    ADD price DECIMAL(19, 2) NOT NULL;
ALTER TABLE order_line_item DROP menu_id;
