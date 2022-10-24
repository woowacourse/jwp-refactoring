INSERT INTO menu_group (name) values ('메뉴그룹1');
INSERT INTO menu_group (name) values ('메뉴그룹2');
INSERT INTO menu_group (name) values ('메뉴그룹3');

INSERT INTO product (name, price) VALUES ('후라이드', 16000);
INSERT INTO product (name, price) VALUES ('양념치킨', 17000);
INSERT INTO product (name, price) VALUES ('광어초밥', 15000);
INSERT INTO product (name, price) VALUES ('연어초밥', 18000);
INSERT INTO product (name, price) VALUES ('육회', 12000);

INSERT INTO menu (name, price, menu_group_id) VALUES ('반반치킨셋트', 33000, 1);
INSERT INTO menu (name, price, menu_group_id) VALUES ('모듬초밥셋트', 33000, 2);

INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (1, 1, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (1, 2, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (2, 3, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (2, 4, 1);

INSERT INTO table_group (created_date) VALUES ('2022-10-10 09:00:00');
INSERT INTO table_group (created_date) VALUES ('2022-09-10 09:00:00');

INSERT INTO order_table (number_of_guests, empty) VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty) VALUES (1, false);
INSERT INTO order_table (number_of_guests, empty) VALUES (2, false);
INSERT INTO order_table (table_group_id, number_of_guests, empty) VALUES (1, 4, false);
INSERT INTO order_table (table_group_id, number_of_guests, empty) VALUES (1, 3, false);

INSERT INTO orders (order_table_id, order_status, ordered_time) VALUES (2, 'COOKING', '2022-10-10 09:00:00');
INSERT INTO orders (order_table_id, order_status, ordered_time) VALUES (3, 'MEAL', '2022-10-10 09:00:00');
INSERT INTO orders (order_table_id, order_status, ordered_time) VALUES (4, 'COMPLETION', '2022-10-10 09:00:00');

INSERT INTO order_line_item (order_id, menu_id, quantity) VALUES (1, 1, 1);
INSERT INTO order_line_item (order_id, menu_id, quantity) VALUES (1, 2, 1);
INSERT INTO order_line_item (order_id, menu_id, quantity) VALUES (2, 2, 2);
INSERT INTO order_line_item (order_id, menu_id, quantity) VALUES (3, 1, 2);
