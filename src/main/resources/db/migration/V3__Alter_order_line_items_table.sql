alter table order_line_item
    add column name varchar(255) null;
alter table order_line_item
    add column price decimal(19, 2) null;

update order_line_item
set name = (select m.name from menu m where m.id = order_line_item.menu_id),
    price = (select m.price from menu m where m.id = order_line_item.menu_id)
where name is null;

alter table order_line_item
alter column name set not null;
alter table order_line_item
alter column price set not null;
