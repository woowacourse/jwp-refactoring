alter table order_line_item
    drop constraint fk_order_line_item_to_menu;

alter table order_line_item
    add column price decimal(19, 2) not null;

alter table order_line_item
    add column name VARCHAR(255) not null;

