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

INSERT INTO order_table (number_of_guests, empty) VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty) VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty) VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty) VALUES (0, true);
