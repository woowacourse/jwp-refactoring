create table ordered_menu
(
    id    bigint         not null auto_increment,
    name  varchar(255)   not null,
    price decimal(19, 2) not null,
    primary key (id)
);

alter table order_line_item
    drop constraint fk_order_line_item_to_menu;

alter table order_line_item
    alter column menu_id rename to ordered_menu_id;

alter table order_line_item
    add constraint fk_order_line_item_to_menu
        foreign key (ordered_menu_id)
            references ordered_menu (id);

