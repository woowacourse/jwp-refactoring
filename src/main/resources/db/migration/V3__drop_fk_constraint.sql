alter table order_line_item
    drop constraint fk_order_line_item_to_menu;

alter table menu
    drop constraint fk_menu_to_menu_group;

alter table menu_product
    drop constraint fk_menu_product_to_product;

alter table orders
    drop constraint fk_orders_to_order_table;

alter table order_table
    drop constraint fk_order_table_to_table_group;

alter table order_line_item
    drop column menu_id;

alter table order_line_item
    add column name varchar(255) not null;

alter table order_line_item
    add column price decimal(19, 2) not null;
