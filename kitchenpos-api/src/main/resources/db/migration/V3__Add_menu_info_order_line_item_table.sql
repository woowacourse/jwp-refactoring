ALTER TABLE order_line_item
    ADD column menu_name VARCHAR(255);

ALTER TABLE order_line_item
    ADD column menu_price DECIMAL(19, 2);
