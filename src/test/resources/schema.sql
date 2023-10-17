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

