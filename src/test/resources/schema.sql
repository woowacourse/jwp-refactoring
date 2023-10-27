create table if not exists menu
(
    id            bigint         not null auto_increment,
    name          varchar(255)   not null,
    price         decimal(19, 2) not null,
    menu_group_id bigint         not null,
    primary key (id)
);

create table if not exists menu_group
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
);

create table if not exists menu_product
(
    seq        bigint not null auto_increment,
    quantity   bigint not null,
    menu_id    bigint not null,
    product_id bigint not null,
    primary key (seq)
);

create table if not exists order_line_item
(
    seq      bigint not null auto_increment,
    quantity bigint not null,
    menu_id  bigint not null,
    order_id bigint not null,
    name     varchar(255) not null,
    price    decimal(19, 2) not null,
    primary key (seq)
);

create table if not exists orders
(
    id             bigint       not null auto_increment,
    order_status   varchar(255) not null,
    ordered_time   datetime     not null,
    order_table_id bigint       not null,
    primary key (id)
);

create table if not exists order_table
(
    id               bigint  not null auto_increment,
    empty            bit     not null,
    number_of_guests integer not null,
    table_group_id   bigint,
    primary key (id)
);

create table if not exists product
(
    id    bigint         not null auto_increment,
    name  varchar(255)   not null,
    price decimal(19, 2) not null,
    primary key (id)
);

create table if not exists table_group
(
    id           bigint   not null auto_increment,
    created_date datetime not null,
    primary key (id)
);

alter table menu
    add constraint if not exists fk_menu_to_menu_group
        foreign key (menu_group_id)
            references menu_group (id);

alter table menu_product
    add constraint if not exists fk_menu_product_to_menu
        foreign key (menu_id)
            references menu (id);

alter table menu_product
    add constraint if not exists fk_menu_product_to_product
        foreign key (product_id)
            references product (id);

alter table order_line_item
    add constraint if not exists fk_order_line_item_to_menu
        foreign key (menu_id)
            references menu (id);

alter table order_line_item
    add constraint if not exists fk_order_line_item_to_orders
        foreign key (order_id)
            references orders (id);

alter table orders
    add constraint if not exists fk_orders_to_order_table
        foreign key (order_table_id)
            references order_table (id);

alter table order_table
    add constraint if not exists fk_order_table_to_table_group
        foreign key (table_group_id)
            references table_group (id);

