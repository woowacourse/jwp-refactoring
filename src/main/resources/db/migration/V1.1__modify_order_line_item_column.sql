alter table order_line_item drop column menu_id;
alter table order_line_item add column product_name varchar(255) not null;
alter table order_line_item add column product_price decimal(19, 2) not null;