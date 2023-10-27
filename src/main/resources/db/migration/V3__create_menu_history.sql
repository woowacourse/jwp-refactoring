create table menu_history
(
    id    bigint         not null auto_increment,
    name  varchar(255)   not null,
    price decimal(19, 2) not null,
    primary key (id)
);

create table menu_product_history
(
    id              bigint         not null auto_increment,
    name            varchar(255)   not null,
    price           decimal(19, 2) not null,
    quantity        bigint         not null,
    menu_history_id bigint         not null,
    primary key (id)
);

alter table menu_product_history
    add constraint fk_menu_product_history_to_menu_history
        foreign key (menu_history_id)
            references menu_history (id);

alter table order_line_item
    add column menu_history_id
        bigint;

alter table order_line_item
    add constraint fk_order_line_item_to_menu_history
        foreign key (menu_history_id)
            references menu_history (id);
