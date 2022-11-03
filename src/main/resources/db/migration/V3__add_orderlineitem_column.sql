alter table order_line_item
    add menu_name VARCHAR(255) not null;
alter table order_line_item
    add menu_price DECIMAL(19,2) not null;
