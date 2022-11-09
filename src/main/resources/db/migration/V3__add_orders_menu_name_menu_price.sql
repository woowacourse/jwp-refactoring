ALTER TABLE orders DROP CONSTRAINT fk_orders_order_table;
ALTER TABLE order_line_item
    ADD menu_name VARCHAR(255);
ALTER TABLE order_line_item
    ADD price DECIMAL(19, 2);
ALTER TABLE order_line_item DROP COLUMN menu_id;