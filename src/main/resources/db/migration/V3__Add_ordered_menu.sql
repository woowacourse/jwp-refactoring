CREATE TABLE ordered_menu (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    menu_id BIGINT(20) NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE order_line_item DROP FOREIGN KEY fk_order_line_item_menu;

ALTER TABLE order_line_item MODIFY COLUMN menu_id BIGINT(20);

ALTER TABLE order_line_item
    ADD ordered_menu_id BIGINT(20) NOT NULL;

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_ordered_menu
        FOREIGN KEY (ordered_menu_id) REFERENCES ordered_menu (id);

ALTER TABLE ordered_menu
    ADD CONSTRAINT fk_order_line_item_menu
        FOREIGN KEY (menu_id) REFERENCES menu (id);
