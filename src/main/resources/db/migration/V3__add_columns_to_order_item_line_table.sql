ALTER TABLE order_line_item
    ADD COLUMN menu_name varchar(255) NOT NULL DEFAULT 'MENU_NAME';

ALTER TABLE order_line_item
    ADD COLUMN menu_price DECIMAL(19, 2) NOT NULL DEFAULT 0;

ALTER TABLE order_line_item DROP CONSTRAINT fk_order_line_item_menu;
