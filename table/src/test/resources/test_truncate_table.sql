SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE order_line_item RESTART IDENTITY;
TRUNCATE TABLE orders RESTART IDENTITY;
TRUNCATE TABLE order_table RESTART IDENTITY;
TRUNCATE TABLE table_group RESTART IDENTITY;
TRUNCATE TABLE menu_product RESTART IDENTITY;
TRUNCATE TABLE menu RESTART IDENTITY;
TRUNCATE TABLE menu_group RESTART IDENTITY;
TRUNCATE TABLE product RESTART IDENTITY;

SET REFERENTIAL_INTEGRITY TRUE;
