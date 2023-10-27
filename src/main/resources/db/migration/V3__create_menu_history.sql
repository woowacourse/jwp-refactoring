create table menu_history
(
    id       bigint         not null auto_increment,
    name     varchar(255)   not null,
    price    decimal(19, 2) not null,
    order_id bigint         not null,
    primary key (id)
);

create table menu_product_history
(
    seq             bigint         not null auto_increment,
    name            varchar(255)   not null,
    price           decimal(19, 2) not null,
    quantity        bigint         not null,
    menu_history_id bigint         not null,
    primary key (seq)
);

alter table menu_history
    add constraint fk_menu_history_to_orders
        foreign key (order_id)
            references orders (id);

alter table menu_product_history
    add constraint fk_menu_product_history_to_menu_history
        foreign key (menu_history_id)
            references menu_history (id);
