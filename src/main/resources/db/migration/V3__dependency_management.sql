ALTER TABLE orders
    DROP CONSTRAINT fk_orders_order_table;

ALTER TABLE menu
    DROP CONSTRAINT fk_menu_menu_group;

ALTER TABLE order_table
    DROP CONSTRAINT fk_order_table_table_group;

ALTER TABLE menu_product
    DROP CONSTRAINT fk_menu_product_product;