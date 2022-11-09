ALTER TABLE order_line_item
    DROP CONSTRAINT fk_order_line_item_orders;

ALTER TABLE order_line_item
    RENAME COLUMN menu_id TO order_menu_id;

CREATE TABLE order_menu
(
    id      BIGINT(20) NOT NULL AUTO_INCREMENT,
    menu_id BIGINT(20),
    created_time   DATETIME     NOT NULL,
    name    VARCHAR(255)   NOT NULL,
    price   DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_order_menu
        FOREIGN KEY (order_menu_id) REFERENCES order_menu (id);

ALTER TABLE order_menu
    ADD CONSTRAINT fk_order_menu_menu
        FOREIGN KEY (menu_id) REFERENCES menu (id);

INSERT INTO order_menu (id, menu_id, name, price)
SELECT 1, 1, name, price FROM menu WHERE menu.id = 1;
    INSERT INTO order_menu (id, menu_id, name, price)
SELECT 2, 2, name, price FROM menu WHERE menu.id = 2;
    INSERT INTO order_menu (id, menu_id, name, price)
SELECT 3, 3, name, price FROM menu WHERE menu.id = 3;
    INSERT INTO order_menu (id, menu_id, name, price)
SELECT 4, 4, name, price FROM menu WHERE menu.id = 4;
    INSERT INTO order_menu (id, menu_id, name, price)
SELECT 5, 5, name, price FROM menu WHERE menu.id = 5;
    INSERT INTO order_menu (id, menu_id, name, price)
SELECT 6, 6, name, price FROM menu WHERE menu.id = 6;
