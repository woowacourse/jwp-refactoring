ALTER TABLE order_line_item DROP CONSTRAINT fk_order_line_item_menu;

ALTER TABLE order_line_item RENAME COLUMN menu_id TO order_menu_id;

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_order_menu
        FOREIGN KEY (order_menu_id) REFERENCES order_menu (id);
