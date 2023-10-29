create table menu
(
    id            bigint         not null auto_increment,
    name          varchar(255)   not null,
    price         decimal(19, 2) not null,
    menu_group_id bigint         not null,
    primary key (id)
);

create table menu_group
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
);

create table menu_product
(
    seq        bigint not null auto_increment,
    quantity   bigint not null,
    menu_id    bigint not null,
    name       varchar(255) not null,
    price      decimal(19, 2) not null,
    primary key (seq)
);

create table order_line_item
(
    seq      bigint not null auto_increment,
    quantity bigint not null,
    ordered_menu_id  bigint not null,
    order_id bigint not null,
    primary key (seq)
);

create table orders
(
    id             bigint       not null auto_increment,
    order_status   varchar(255) not null,
    ordered_time   datetime     not null,
    primary key (id)
);

create table order_table
(
    id               bigint  not null auto_increment,
    empty            bit     not null,
    number_of_guests integer not null,
    table_group_id   bigint,
    primary key (id)
);

create table product
(
    id    bigint         not null auto_increment,
    name  varchar(255)   not null,
    price decimal(19, 2) not null,
    primary key (id)
);

create table table_group
(
    id           bigint   not null auto_increment,
    created_date datetime not null,
    primary key (id)
);

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
