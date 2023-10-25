alter table order_line_item add column name varchar(255);
alter table order_line_item add column price decimal(19, 2);

update order_line_item oli
set (name, price) = (
    select m.name, m.price
    from menu m
    where oli.menu_id = m.id
    )
where exists (
    select 1
    from menu m
    where oli.menu_id = m.id
    );

alter table order_line_item alter column name set data type varchar(255);
alter table order_line_item alter column name set not null;
alter table order_line_item alter column price set data type decimal(19, 2);
alter table order_line_item alter column price set not null;
