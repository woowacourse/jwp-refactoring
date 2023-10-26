alter table order_line_item drop column menu_id;
alter table order_line_item add column ordered_menu_id bigint;

create table ordered_menu
(
    id bigint not null auto_increment,
    name varchar(255) not null,
    price decimal(19, 2) not null,
    menu_group_name varchar(255) not null,
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
