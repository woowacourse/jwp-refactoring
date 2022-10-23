CREATE TABLE IF NOT EXISTS orders
(
    id             BIGINT(20) NOT NULL AUTO_INCREMENT,
    order_TABLE_id BIGINT(20) NOT NULL,
    order_status   VARCHAR(255) NOT NULL,
    ordered_time   DATETIME     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS order_line_item
(
    seq      BIGINT(20) NOT NULL AUTO_INCREMENT,
    order_id BIGINT(20) NOT NULL,
    menu_id  BIGINT(20) NOT NULL,
    quantity BIGINT(20) NOT NULL,
    PRIMARY KEY (seq)
);

CREATE TABLE IF NOT EXISTS menu
(
    id            BIGINT(20) NOT NULL AUTO_INCREMENT,
    name          VARCHAR(255)   NOT NULL,
    price         DECIMAL(19, 2) NOT NULL,
    menu_group_id BIGINT(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS menu_group
(
    id   BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS menu_product
(
    seq        BIGINT(20) NOT NULL AUTO_INCREMENT,
    menu_id    BIGINT(20) NOT NULL,
    product_id BIGINT(20) NOT NULL,
    quantity   BIGINT(20) NOT NULL,
    PRIMARY KEY (seq)
);

CREATE TABLE IF NOT EXISTS order_TABLE
(
    id               BIGINT(20) NOT NULL AUTO_INCREMENT,
    TABLE_group_id   BIGINT(20),
    number_of_guests INT(11) NOT NULL,
    empty            BIT(1) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS TABLE_group
(
    id           BIGINT(20) NOT NULL AUTO_INCREMENT,
    created_date DATETIME NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS product
(
    id    BIGINT(20) NOT NULL AUTO_INCREMENT,
    name  VARCHAR(255)   NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE orders
    ADD CONSTRAINT IF NOT EXISTS fk_orders_order_TABLE
        FOREIGN KEY (order_TABLE_id) REFERENCES order_TABLE (id);

ALTER TABLE order_line_item
    ADD CONSTRAINT IF NOT EXISTS fk_order_line_item_orders
        FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_line_item
    ADD CONSTRAINT IF NOT EXISTS fk_order_line_item_menu
        FOREIGN KEY (menu_id) REFERENCES menu (id);

ALTER TABLE menu
    ADD CONSTRAINT IF NOT EXISTS fk_menu_menu_group
        FOREIGN KEY (menu_group_id) REFERENCES menu_group (id);

ALTER TABLE menu_product
    ADD CONSTRAINT IF NOT EXISTS fk_menu_product_menu
        FOREIGN KEY (menu_id) REFERENCES menu (id);

ALTER TABLE menu_product
    ADD CONSTRAINT IF NOT EXISTS fk_menu_product_product
        FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE order_TABLE
    ADD CONSTRAINT IF NOT EXISTS fk_order_TABLE_TABLE_group
        FOREIGN KEY (TABLE_group_id) REFERENCES TABLE_group (id);

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE order_line_item;

TRUNCATE TABLE orders;
ALTER TABLE orders ALTER COLUMN id RESTART WITH 1;

TRUNCATE TABLE order_TABLE;
ALTER TABLE order_TABLE ALTER COLUMN id RESTART WITH 1;

TRUNCATE TABLE TABLE_group;
ALTER TABLE TABLE_group ALTER COLUMN id RESTART WITH 1;

TRUNCATE TABLE menu_product;

TRUNCATE TABLE product;
ALTER TABLE product ALTER COLUMN id RESTART WITH 1;

TRUNCATE TABLE menu;
ALTER TABLE menu ALTER COLUMN id RESTART WITH 1;

TRUNCATE TABLE menu_group;
ALTER TABLE menu_group ALTER COLUMN id RESTART WITH 1;

SET FOREIGN_KEY_CHECKS = 1;
