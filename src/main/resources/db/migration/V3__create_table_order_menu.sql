CREATE TABLE order_menu
(
    id              BIGINT(20) NOT NULL AUTO_INCREMENT,
    name            VARCHAR(255)   NOT NULL,
    price           DECIMAL(19, 2) NOT NULL,
    menu_group_name VARCHAR(255)   NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE order_line_item
DROP
FOREIGN KEY fk_order_line_item_menu;

ALTER TABLE order_line_item
    RENAME COLUMN menu_id TO order_menu_id;

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_order_menu
        FOREIGN KEY (order_menu_id) REFERENCES order_menu (id);

CREATE TABLE order_product
(
    seq           BIGINT(20) NOT NULL AUTO_INCREMENT,
    order_menu_id BIGINT(20) NOT NULL,
    name          VARCHAR(255)   NOT NULL,
    price         DECIMAL(19, 2) NOT NULL
);

ALTER TABLE order_product
    ADD CONSTRAINT fk_order_product_order_menu
        FOREIGN KEY (order_menu_id) REFERENCES order_menu (id);
