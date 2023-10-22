ALTER TABLE menu_product
    ADD COLUMN menu_key BIGINT;

ALTER TABLE order_line_item
    ADD COLUMN ORDER_KEY BIGINT;

ALTER TABLE orders
    ADD COLUMN order_table_key BIGINT;
