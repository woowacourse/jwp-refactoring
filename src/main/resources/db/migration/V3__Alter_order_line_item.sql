ALTER TABLE order_line_item
    ADD menu_name VARCHAR(255) NOT NULL;

ALTER TABLE order_line_item
    ADD menu_price DECIMAL(19, 2) NOT NULL;

INSERT INTO orders (order_table_id, order_status, ordered_time)
VALUES (1, 'COOKING', now());

INSERT INTO order_line_item (order_id, menu_id, menu_name, menu_price, quantity)
VALUES (1, 1, 'pasta', 13000, 3);
