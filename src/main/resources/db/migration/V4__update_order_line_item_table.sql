alter table order_line_item
    add column name varchar(255);

alter table order_line_item
    add column price decimal(19, 2);

update order_line_item
set price = (select m.price from menu as m where m.id = menu_id),
    name  = (select m.name from menu as m where m.id = menu_id);

alter table order_line_item
    alter column name set not null;

alter table order_line_item
    alter column price set not null;
