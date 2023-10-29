create table menu_history
(
    id       bigint         not null auto_increment,
    name     varchar(255)   not null,
    price    decimal(19, 2) not null,
    order_id bigint         not null,
    primary key (id)
);

alter table menu_history
    add constraint fk_menu_history_to_orders
        foreign key (order_id)
            references orders (id);
