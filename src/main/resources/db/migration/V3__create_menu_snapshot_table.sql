create table menu_snapshot
(
    id                 bigint         not null auto_increment,
    name               varchar(255)   not null,
    price              decimal(19, 2) not null,
    order_line_item_id bigint         not null,
    primary key (id)
);

alter table order_line_item
    drop foreign key fk_order_line_item_to_menu;

alter table order_line_item
    drop menu_id;

alter table order_line_item
    add column menu_snapshot_id bigint not null;

alter table order_line_item
    add constraint fk_order_line_item_to_menu_snapshot
        foreign key (menu_snapshot_id)
            references menu_snapshot (id);

create table menu_product_snapshot
(
    id               bigint         not null auto_increment,
    name             varchar(255)   not null,
    price            decimal(19, 2) not null,
    quantity         bigint         not null,
    menu_snapshot_id bigint         not null,
    primary key (id)
);

alter table menu_product_snapshot
    add constraint fk_menu_product_history_menu_history
        foreign key (menu_snapshot_id)
            references menu_product_snapshot (id);
