ALTER TABLE order_line_item
    ADD COLUMN menu_name VARCHAR(255) NOT NULL;

ALTER TABLE order_line_item
    ADD COLUMN price DECIMAL(19, 2) NOT NULL;

ALTER TABLE order_line_item
    DROP COLUMN menu_id;
