alter table order_line_item add price decimal(19, 2);
alter table order_line_item add name varchar(255);

update order_line_item oli
set (oli.name, oli.price) = (
    select m.name, m.price
    from menu m
    where m.id = oli.menu_id
)
where exists (
    select 1
    from menu m
    where m.id = oli.menu_id
);
