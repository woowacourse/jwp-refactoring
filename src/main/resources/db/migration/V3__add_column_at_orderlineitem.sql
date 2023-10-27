alter table order_line_item
    add original_menu_name varchar(255) not null after menu_id;

alter table order_line_item
    add original_menu_price decimal(19,2) not null after original_menu_name;

alter table order_line_item
    add original_menu_group_id bigint not null after original_menu_price;
