alter table menu drop CONSTRAINT fk_menu_to_menu_group;

alter table order_line_item drop CONSTRAINT fk_order_line_item_to_menu;

alter table orders drop CONSTRAINT fk_orders_to_order_table;

alter table order_table drop CONSTRAINT fk_order_table_to_table_group;
