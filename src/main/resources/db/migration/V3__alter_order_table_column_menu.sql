create table menu_history
(
    id    bigint         not null auto_increment,
    name  varchar(255)   not null,
    price decimal(19, 2) not null,
    primary key (id)
);

alter table order_line_item
    drop foreign key fk_order_line_item_to_menu;

alter table order_line_item
    drop menu_id;

alter table order_line_item
    add column menu_history_id bigint not null;

alter table menu
    add column menu_history_id bigint not null;

insert into menu_history(name, price)
select name, price
from menu;

update menu
set menu.menu_history_id = (select id
                            from menu_history
                            where menu.price = menu_history.price and menu.name = menu_history.name)
where id > 0;

alter table order_line_item
    add constraint fk_order_to_menu_history
        foreign key (menu_history_id)
            references menu_history (id);

alter table menu
    add constraint fk_menu_to_menu_history
        foreign key (menu_history_id)
            references menu_history (id);

