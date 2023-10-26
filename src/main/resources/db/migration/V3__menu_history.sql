alter table order_line_item
    add column menu_price BIGINT;
alter table order_line_item
    add column menu_name VARCHAR(255);
