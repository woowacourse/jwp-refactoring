alter table order_line_item add column item_name varchar(255) not null;
alter table order_line_item add column price numeric(19,2) not null;

UPDATE order_line_item SET item_name = ( SELECT name FROM menu WHERE menu.id = order_line_item.menu_id);
UPDATE order_line_item SET price = ( SELECT price FROM menu WHERE menu.id = order_line_item.menu_id);
