SET REFERENTIAL_INTEGRITY FALSE;

delete from menu;
delete from menu_group;
delete from menu_product;
delete from order_line_item;
delete from orders;
delete from order_table;
delete from product;
delete from table_group;

SET REFERENTIAL_INTEGRITY TRUE;