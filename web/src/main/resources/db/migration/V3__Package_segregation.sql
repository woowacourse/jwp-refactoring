alter table menu_product add column name varchar(255);
alter table menu_product add column price decimal(19,2);
UPDATE menu_product mp
SET mp.name = (SELECT p.name FROM product p WHERE mp.product_id = p.id),
    mp.price = (SELECT p.price FROM product p WHERE mp.product_id = p.id);

alter table menu_product drop column product_id;

alter table order_line_item drop column menu_id;
alter table order_line_item add column ordered_menu_id bigint;

alter table orders drop column order_table_id;

create table ordered_menu
(
    id bigint not null auto_increment,
    name varchar(255) not null,
    price decimal(19, 2) not null,
    ordered_menu_group_name varchar(255) not null,
    primary key (id)
);

create table ordered_menu_product
(
    seq bigint not null auto_increment,
    ordered_menu_id bigint not null,
    name varchar(255) not null,
    price decimal(19, 2) not null,
    quantity bigint not null,
    primary key (seq)
);

create table order_table_log
(
    id bigint not null auto_increment,
    order_id bigint not null,
    order_table_id bigint not null,
    number_of_guests integer not null,
    primary key (id)
);
