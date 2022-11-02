ALTER TABLE order_line_item
    DROP CONSTRAINT fk_order_line_item_menu;

ALTER TABLE order_line_item
    DROP COLUMN menu_id;

ALTER TABLE order_line_item
    ADD COLUMN name VARCHAR(255) NOT NULL;

ALTER TABLE order_line_item
    ADD COLUMN price DECIMAL(19, 2) NOT NULL;
