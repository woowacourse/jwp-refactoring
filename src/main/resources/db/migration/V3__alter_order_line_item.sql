ALTER TABLE order_line_item
    DROP CONSTRAINT fk_order_line_item_to_menu;

ALTER TABLE order_line_item
    ADD menu_name VARCHAR(255) NOT NULL;

ALTER TABLE order_line_item
    ADD price DECIMAL(19, 2) NOT NULL;
