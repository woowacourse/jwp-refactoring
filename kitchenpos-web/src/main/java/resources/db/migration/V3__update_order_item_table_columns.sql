
alter table order_line_item add menu_name varchar(255);
alter table order_line_item add menu_price decimal(19, 2);

alter table order_line_item drop constraint fk_order_line_item_to_menu;
alter table order_line_item drop menu_id;
