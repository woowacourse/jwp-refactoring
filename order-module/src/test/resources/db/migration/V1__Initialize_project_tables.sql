create table order_line_item
(
    seq      bigint not null auto_increment,
    quantity bigint not null,
    menu_id  bigint not null,
    order_id bigint not null,
    primary key (seq)
);

create table orders
(
    id             bigint       not null auto_increment,
    order_status   varchar(255) not null,
    ordered_time   datetime     not null,
    order_table_id bigint       not null,
    primary key (id)
);
alter table order_line_item
    add constraint fk_order_line_item_to_menu
        foreign key (menu_id)
            references menu (id);

alter table order_line_item
    add constraint fk_order_line_item_to_orders
        foreign key (order_id)
            references orders (id);

alter table orders
    add constraint fk_orders_to_order_table
        foreign key (order_table_id)
            references order_table (id);
