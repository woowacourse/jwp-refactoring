SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_line_item;
TRUNCATE TABLE orders;
TRUNCATE TABLE order_table;
ALTER TABLE order_table ALTER COLUMN id RESTART WITH 1;
TRUNCATE TABLE table_group;
TRUNCATE TABLE menu_product;
ALTER TABLE menu_product ALTER COLUMN seq RESTART WITH 1;
TRUNCATE TABLE menu;
ALTER TABLE menu ALTER COLUMN id RESTART WITH 1;
TRUNCATE TABLE menu_group;
ALTER TABLE menu_group ALTER COLUMN id RESTART WITH 1;
TRUNCATE TABLE product;
ALTER TABLE product ALTER COLUMN id RESTART WITH 1;
SET FOREIGN_KEY_CHECKS = 1;
