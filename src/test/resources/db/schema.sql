CREATE TABLE orders (
                        id BIGINT(20) NOT NULL AUTO_INCREMENT,
                        order_table_id BIGINT(20) NOT NULL,
                        order_status VARCHAR(255) NOT NULL,
                        ordered_time DATETIME NOT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE order_line_item (
                                 seq BIGINT(20) NOT NULL AUTO_INCREMENT,
                                 order_id BIGINT(20) NOT NULL,
                                 menu_id BIGINT(20) NOT NULL,
                                 quantity BIGINT(20) NOT NULL,
                                 PRIMARY KEY (seq)
);

CREATE TABLE menu (
                      id BIGINT(20) NOT NULL AUTO_INCREMENT,
                      name VARCHAR(255) NOT NULL,
                      price DECIMAL(19, 2) NOT NULL,
                      menu_group_id BIGINT(20) NOT NULL,
                      PRIMARY KEY (id)
);

CREATE TABLE menu_group (
                            id BIGINT(20) NOT NULL AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL,
                            PRIMARY KEY (id)
);

CREATE TABLE menu_product (
                              seq BIGINT(20) NOT NULL AUTO_INCREMENT,
                              menu_id BIGINT(20) NOT NULL,
                              product_id BIGINT(20) NOT NULL,
                              quantity BIGINT(20) NOT NULL,
                              PRIMARY KEY (seq)
);

CREATE TABLE order_table (
                             id BIGINT(20) NOT NULL AUTO_INCREMENT,
                             table_group_id BIGINT(20),
                             number_of_guests INT(11) NOT NULL,
                             empty BIT(1) NOT NULL,
                             PRIMARY KEY (id)
);

CREATE TABLE table_group (
                             id BIGINT(20) NOT NULL AUTO_INCREMENT,
                             created_date DATETIME NOT NULL,
                             PRIMARY KEY (id)
);

CREATE TABLE product (
                         id BIGINT(20) NOT NULL AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL,
                         price DECIMAL(19, 2) NOT NULL,
                         PRIMARY KEY (id)
);

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_order_table
        FOREIGN KEY (order_table_id) REFERENCES order_table (id);

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_orders
        FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_menu
        FOREIGN KEY (menu_id) REFERENCES menu (id);

ALTER TABLE menu
    ADD CONSTRAINT fk_menu_menu_group
        FOREIGN KEY (menu_group_id) REFERENCES menu_group (id);

ALTER TABLE menu_product
    ADD CONSTRAINT fk_menu_product_menu
        FOREIGN KEY (menu_id) REFERENCES menu (id);

ALTER TABLE menu_product
    ADD CONSTRAINT fk_menu_product_product
        FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE order_table
    ADD CONSTRAINT fk_order_table_table_group
        FOREIGN KEY (table_group_id) REFERENCES table_group (id);

INSERT INTO menu_group (id, name) VALUES (1, '두마리메뉴');
INSERT INTO menu_group (id, name) VALUES (2, '한마리메뉴');
INSERT INTO menu_group (id, name) VALUES (3, '순살파닭두마리메뉴');
INSERT INTO menu_group (id, name) VALUES (4, '신메뉴');

INSERT INTO product (id, name, price) VALUES (1, '후라이드', 16000);
INSERT INTO product (id, name, price) VALUES (2, '양념치킨', 16000);
INSERT INTO product (id, name, price) VALUES (3, '반반치킨', 16000);
INSERT INTO product (id, name, price) VALUES (4, '통구이', 16000);
INSERT INTO product (id, name, price) VALUES (5, '간장치킨', 17000);
INSERT INTO product (id, name, price) VALUES (6, '순살치킨', 17000);

INSERT INTO menu (id, name, price, menu_group_id) VALUES (1, '후라이드치킨', 16000, 2);
INSERT INTO menu (id, name, price, menu_group_id) VALUES (2, '양념치킨', 16000, 2);
INSERT INTO menu (id, name, price, menu_group_id) VALUES (3, '반반치킨', 16000, 2);
INSERT INTO menu (id, name, price, menu_group_id) VALUES (4, '통구이', 16000, 2);
INSERT INTO menu (id, name, price, menu_group_id) VALUES (5, '간장치킨', 17000, 2);
INSERT INTO menu (id, name, price, menu_group_id) VALUES (6, '순살치킨', 17000, 2);

INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (1, 1, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (2, 2, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (3, 3, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (4, 4, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (5, 5, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (6, 6, 1);

INSERT INTO order_table (id, number_of_guests, empty) VALUES (1, 0, true);
INSERT INTO order_table (id, number_of_guests, empty) VALUES (2, 0, true);
INSERT INTO order_table (id, number_of_guests, empty) VALUES (3, 0, true);
INSERT INTO order_table (id, number_of_guests, empty) VALUES (4, 0, true);
INSERT INTO order_table (id, number_of_guests, empty) VALUES (5, 0, true);
INSERT INTO order_table (id, number_of_guests, empty) VALUES (6, 0, true);
INSERT INTO order_table (id, number_of_guests, empty) VALUES (7, 0, true);
INSERT INTO order_table (id, number_of_guests, empty) VALUES (8, 0, true);





