create table menu_product_snapshot
(
    order_line_item_seq bigint not null,
    name                varchar(255),
    price               numeric(19, 2),
    quantity            bigint not null
);

alter table menu_product_snapshot
    add constraint fk_menu_product_snapshot_to_order_line_item
        foreign key (order_line_item_seq)
            references order_line_item;


alter table order_line_item
drop
constraint fk_order_line_item_to_menu;

alter table order_line_item
drop
column menu_id;

alter table order_line_item
    add menu_group_name varchar(255);
alter table order_line_item
    add name varchar(255);
alter table order_line_item
    add price numeric(19, 2);
