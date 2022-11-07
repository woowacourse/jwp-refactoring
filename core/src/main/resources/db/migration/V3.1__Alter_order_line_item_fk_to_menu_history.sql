ALTER TABLE order_line_item
    RENAME COLUMN menu_id TO menu_history_id;

ALTER TABLE order_line_item
    DROP FOREIGN KEY fk_order_line_item_menu;

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_menu_history_id
        FOREIGN KEY (menu_history_id) REFERENCES menu_history (id);
