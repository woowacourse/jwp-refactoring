create table order_menu
(
    id            bigint         not null auto_increment,
    name          varchar(255)   not null,
    price         decimal(19, 2) not null,
    menu_group_id bigint         not null,
    primary key (id)
);

create table order_menu_product
(
    seq        bigint not null auto_increment,
    quantity   bigint not null,
    menu_id    bigint not null,
    product_id bigint not null,
    primary key (seq)
);

alter table order_menu
    add constraint fk_order_menu_to_menu_group
        foreign key (menu_group_id)
            references menu_group (id);

alter table order_menu_product
    add constraint fk_order_menu_product_to_menu
        foreign key (menu_id)
            references menu (id);

alter table order_menu_product
    add constraint fk_order_menu_product_to_product
        foreign key (product_id)
            references product (id);
