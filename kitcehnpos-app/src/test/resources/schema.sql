-- menu 테이블
create table menu
(
    id            bigint         not null auto_increment,
    name          varchar(255)   not null,
    price         decimal(19, 2) not null,
    menu_group_id bigint         not null,
    primary key (id)
);

-- menu_group 테이블
create table menu_group
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
);

-- menu_product 테이블
create table menu_product
(
    seq        bigint not null auto_increment,
    quantity   bigint not null,
    menu_id    bigint not null,
    product_id bigint not null,
    primary key (seq)
);

-- order_line_item 테이블
create table order_line_item
(
    seq      bigint not null auto_increment,
    quantity bigint not null,
    menu_id  bigint not null,
    order_id bigint not null,
    primary key (seq)
);

-- orders 테이블
create table orders
(
    id             bigint       not null auto_increment,
    order_status   varchar(255) not null,
    ordered_time   datetime     not null,
    order_table_id bigint       not null,
    primary key (id)
);

-- order_table 테이블
create table order_table
(
    id               bigint  not null auto_increment,
    empty            bit     not null,
    number_of_guests integer not null,
    table_group_id   bigint,
    primary key (id)
);

-- product 테이블
create table product
(
    id    bigint         not null auto_increment,
    name  varchar(255)   not null,
    price decimal(19, 2) not null,
    primary key (id)
);

-- table_group 테이블
create table table_group
(
    id           bigint   not null auto_increment,
    created_date datetime not null,
    primary key (id)
);

-- menu 테이블의 외래 키 제약 조건
alter table menu
    add constraint fk_menu_to_menu_group
        foreign key (menu_group_id)
            references menu_group (id);

-- menu_product 테이블의 외래 키 제약 조건
alter table menu_product
    add constraint fk_menu_product_to_menu
        foreign key (menu_id)
            references menu (id);

alter table menu_product
    add constraint fk_menu_product_to_product
        foreign key (product_id)
            references product (id);

-- order_line_item 테이블의 외래 키 제약 조건
alter table order_line_item
    add constraint fk_order_line_item_to_menu
        foreign key (menu_id)
            references menu (id);

alter table order_line_item
    add constraint fk_order_line_item_to_orders
        foreign key (order_id)
            references orders (id);

-- orders 테이블의 외래 키 제약 조건
alter table orders
    add constraint fk_orders_to_order_table
        foreign key (order_table_id)
            references order_table (id);

-- order_table 테이블의 외래 키 제약 조건
alter table order_table
    add constraint fk_order_table_to_table_group
        foreign key (table_group_id)
            references table_group (id);
