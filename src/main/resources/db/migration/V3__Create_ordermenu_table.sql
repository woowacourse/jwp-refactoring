CREATE TABLE order_menu (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE order_line_item
    DROP CONSTRAINT fk_order_line_item_menu;

ALTER TABLE order_line_item
    ALTER COLUMN menu_id RENAME TO order_menu_id;

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_order_menu
        FOREIGN KEY (order_menu_id) REFERENCES order_menu (id);
