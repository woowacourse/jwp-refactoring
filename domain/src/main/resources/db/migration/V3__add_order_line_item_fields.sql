alter table order_line_item
    add menu_name varchar(255) not null;

alter table order_line_item
    add menu_price decimal(19, 2) not null;
