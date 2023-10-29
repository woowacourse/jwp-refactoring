-- Description: Truncate all tables
SET REFERENTIAL_INTEGRITY FALSE;
truncate table menu;
truncate table menu_group;
truncate table menu_product;
truncate table order_line_item;
truncate table orders;
truncate table order_table;
truncate table product;
truncate table table_group;
SET REFERENTIAL_INTEGRITY TRUE;
