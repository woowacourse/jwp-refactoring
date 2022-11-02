ALTER TABLE order_line_item
    DROP CONSTRAINT fk_order_line_item_orders;

ALTER TABLE order_line_item
    RENAME COLUMN menu_id TO menu_order_id;

CREATE TABLE menu_order
(
    id    BIGINT(20) NOT NULL AUTO_INCREMENT,
    name  VARCHAR(255)   NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_menu_order
        FOREIGN KEY (menu_order_id) REFERENCES menu (id);

INSERT INTO menu_order (id, name, price)
VALUES (1, '후라이드치킨', 16000);
INSERT INTO menu_order (id, name, price)
VALUES (2, '양념치킨', 16000);
INSERT INTO menu_order (id, name, price)
VALUES (3, '반반치킨', 16000);
INSERT INTO menu_order (id, name, price)
VALUES (4, '통구이', 16000);
INSERT INTO menu_order (id, name, price)
VALUES (5, '간장치킨', 17000);
INSERT INTO menu_order (id, name, price)
VALUES (6, '순살치킨', 17000);
