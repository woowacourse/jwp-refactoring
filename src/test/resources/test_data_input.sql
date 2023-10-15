SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE product RESTART IDENTITY;
TRUNCATE TABLE menu RESTART IDENTITY;
TRUNCATE TABLE menu_group RESTART IDENTITY;
TRUNCATE TABLE menu_product RESTART IDENTITY;
TRUNCATE TABLE order_table RESTART IDENTITY;
TRUNCATE TABLE orders RESTART IDENTITY;
TRUNCATE TABLE order_line_item RESTART IDENTITY;
TRUNCATE TABLE table_group RESTART IDENTITY;

SET REFERENTIAL_INTEGRITY TRUE;

-- product input
INSERT INTO product (name, price)
VALUES ('product1', 10);
INSERT INTO product (name, price)
VALUES ('product2', 20);

-- menu_group input
INSERT INTO menu_group (name)
VALUES ('menu_group1');
INSERT INTO menu_group (name)
VALUES ('menu_group2');

-- menu input
INSERT INTO menu (name, price, menu_group_id)
VALUES ('menu1', 30 , 1);
INSERT INTO menu (name, price, menu_group_id)
VALUES ('menu2', 30, 1);

-- menu_product input
INSERT INTO menu_product (menu_id, product_id, quantity)
VALUES (1, 1, 1);
INSERT INTO menu_product (menu_id, product_id, quantity)
VALUES (2, 2, 1);

-- order_table input
INSERT INTO order_table (number_of_guests, empty)
VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty)
VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty)
VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty)
VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty)
VALUES (0, false);

-- orders input
INSERT INTO orders (order_status, ordered_time, order_table_id)
VALUES ('COOKING',now(),3);
INSERT INTO orders (order_status, ordered_time, order_table_id)
VALUES ('MEAL',now(),4);

-- order_line_item input
INSERT INTO order_line_item(quantity,menu_id,order_id)
VALUES (1,1,1);
