drop table if exists orders;
drop table if exists order_line_item;
drop table if exists menu;
drop table if exists menu_group;
drop table if exists menu_product;
drop table if exists order_table;
drop table if exists table_group;
drop table if exists product;

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
