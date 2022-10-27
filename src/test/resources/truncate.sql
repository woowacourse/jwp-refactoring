SET foreign_key_checks = 0;

TRUNCATE TABLE orders;
TRUNCATE TABLE order_line_item;
TRUNCATE TABLE menu;
TRUNCATE TABLE menu_group;
TRUNCATE TABLE menu_product;
TRUNCATE TABLE order_table;
TRUNCATE TABLE table_group;
TRUNCATE TABLE product;

SET foreign_key_checks = 1;
