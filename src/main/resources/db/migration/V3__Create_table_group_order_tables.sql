create table table_group_order_tables
(
    id              bigint not null auto_increment primary key,
    table_group_id  bigint not null,
    order_tables_id bigint not null
);
create table orders_order_line_items
(
    id                   bigint not null auto_increment primary key,
    orders_id            bigint not null,
    order_line_items_seq bigint not null
);
