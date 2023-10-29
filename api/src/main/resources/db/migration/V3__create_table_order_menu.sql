create table order_menu
(
    id            bigint         not null auto_increment,
    menu_id       bigint         not null,
    name          varchar(255)   not null,
    price         decimal(19, 2) not null,
    primary key (id)
);

create table order_product
(
    seq        bigint not null auto_increment,
    quantity   bigint not null,
    order_menu_id    bigint not null,
    product_id bigint not null,
    name  varchar(255)   not null,
    price decimal(19, 2) not null,
    primary key (seq)
);

alter table order_product
    add constraint fk_order_product_to_order_menu
        foreign key (order_menu_id)
            references order_menu (id);

alter table order_line_item drop constraint fk_order_line_item_to_menu;
alter table order_line_item drop column menu_id;
alter table order_line_item add column order_menu_id bigint not null;
alter table order_line_item
    add constraint fk_order_line_item_to_order_menu
        foreign key (order_menu_id)
            references order_menu (id);
