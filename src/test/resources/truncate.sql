SET REFERENTIAL_INTEGRITY FALSE;

truncate table product;
truncate table menu;
truncate table menu_group;
truncate table menu_product;
truncate table order_table;
truncate table orders;
truncate table order_line_item;
truncate table table_group;


ALTER TABLE product
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE menu
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE menu_group
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE order_table
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE orders
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE table_group
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE order_line_item
    ALTER COLUMN seq RESTART WITH 1;
ALTER TABLE menu_product
    ALTER COLUMN seq RESTART WITH 1;