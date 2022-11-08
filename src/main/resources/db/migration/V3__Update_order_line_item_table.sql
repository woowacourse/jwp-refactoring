ALTER TABLE order_line_item
    ADD menu_name VARCHAR(255);
ALTER TABLE order_line_item
    ADD price DECIMAL(19, 2);
ALTER TABLE order_line_item DROP COLUMN menu_id;