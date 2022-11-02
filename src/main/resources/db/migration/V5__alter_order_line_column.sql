alter table order_line_item drop constraint fk_order_line_item_menu;

ALTER TABLE order_line_item RENAME COLUMN menu_id TO order_menu_id;

alter table order_line_item alter column order_menu_id set null;
