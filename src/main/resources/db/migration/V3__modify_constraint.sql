alter table menu modify column name varchar(255);
alter table menu modify column price decimal(19,2);

alter table menu_group modify column name varchar(255);

alter table order_line_item add name varchar(255) after menu_id;
alter table order_line_item add price decimal(19,2) after name;

alter table orders modify column order_status varchar(255);

alter table product modify column name varchar(255);
alter table product modify column price decimal(19,2);

alter table menu drop foreign key fk_menu_to_menu_group;

alter table menu_product drop foreign key fk_menu_product_to_menu;
alter table menu_product drop foreign key fk_menu_product_to_product;

alter table order_line_item drop foreign key fk_order_line_item_to_menu;

alter table orders drop foreign key fk_orders_to_order_table;

alter table order_table drop foreign key fk_order_table_to_table_group;
