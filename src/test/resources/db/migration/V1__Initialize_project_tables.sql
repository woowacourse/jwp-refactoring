CREATE TABLE orders (
                        orders_id BIGINT(20) NOT NULL AUTO_INCREMENT,
                        order_table_id BIGINT(20) NOT NULL,
                        order_status VARCHAR(255) NOT NULL,
                        ordered_time DATETIME NOT NULL,
                        PRIMARY KEY (orders_id)
);

CREATE TABLE order_line_item (
                                 order_line_item_id BIGINT(20) NOT NULL AUTO_INCREMENT,
                                 orders_id BIGINT(20) NOT NULL,
                                 menu_id BIGINT(20) NOT NULL,
                                 quantity BIGINT(20) NOT NULL,
                                 PRIMARY KEY (order_line_item_id)
);

CREATE TABLE menu (
                      menu_id BIGINT(20) NOT NULL AUTO_INCREMENT,
                      name VARCHAR(255) NOT NULL,
                      price BIGINT(20) NOT NULL,
                      menu_group_id BIGINT(20) NOT NULL,
                      PRIMARY KEY (menu_id)
);

CREATE TABLE menu_group (
                            menu_group_id BIGINT(20) NOT NULL AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL,
                            PRIMARY KEY (menu_group_id)
);

CREATE TABLE menu_product (
                              menu_product_id BIGINT(20) NOT NULL AUTO_INCREMENT,
                              menu_id BIGINT(20) NOT NULL,
                              product_id BIGINT(20) NOT NULL,
                              quantity BIGINT(20) NOT NULL,
                              PRIMARY KEY (menu_product_id)
);

CREATE TABLE order_table (
                             order_table_id BIGINT(20) NOT NULL AUTO_INCREMENT,
                             table_group_id BIGINT(20),
                             number_of_guests INT(11) NOT NULL,
                             empty BIT(1) NOT NULL,
                             PRIMARY KEY (order_table_id)
);

CREATE TABLE table_group (
                             table_group_id BIGINT(20) NOT NULL AUTO_INCREMENT,
                             created_date DATETIME NOT NULL,
                             PRIMARY KEY (table_group_id)
);

CREATE TABLE product (
                         product_id BIGINT(20) NOT NULL AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL,
                         price BIGINT(20) NOT NULL,
                         PRIMARY KEY (product_id)
);

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_order_table
        FOREIGN KEY (order_table_id) REFERENCES order_table (order_table_id);

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_orders
        FOREIGN KEY (orders_id) REFERENCES orders (orders_id);

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_menu
        FOREIGN KEY (menu_id) REFERENCES menu (menu_id);

ALTER TABLE menu
    ADD CONSTRAINT fk_menu_menu_group
        FOREIGN KEY (menu_group_id) REFERENCES menu_group (menu_group_id);

ALTER TABLE menu_product
    ADD CONSTRAINT fk_menu_product_menu
        FOREIGN KEY (menu_id) REFERENCES menu (menu_id);

ALTER TABLE menu_product
    ADD CONSTRAINT fk_menu_product_product
        FOREIGN KEY (product_id) REFERENCES product (product_id);

ALTER TABLE order_table
    ADD CONSTRAINT fk_order_table_table_group
        FOREIGN KEY (table_group_id) REFERENCES table_group (table_group_id);
