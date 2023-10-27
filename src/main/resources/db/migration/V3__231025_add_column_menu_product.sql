alter table order_line_item
    add menu_name varchar(100) default '상품명';
alter table order_line_item
    add menu_price decimal(19, 2) default 10000;

