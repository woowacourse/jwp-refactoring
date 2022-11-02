ALTER TABLE order_line_item
    ADD COLUMN menu_name VARCHAR(255);

ALTER TABLE order_line_item
    ADD COLUMN menu_price DECIMAL(19, 2);

ALTER TABLE order_line_item
    ADD COLUMN menu_group_name VARCHAR(255);
