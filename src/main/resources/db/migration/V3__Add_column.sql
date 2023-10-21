ALTER TABLE menu_product
    ADD COLUMN menu_key BIGINT;

ALTER TABLE order_line_item
    ADD COLUMN ORDER_KEY BIGINT;
