INSERT INTO menu_group (id, name) VALUES (1, '두마리메뉴');
INSERT INTO menu_group (id, name) VALUES (2, '한마리메뉴');

INSERT INTO product (id, name, price) VALUES (1, '후라이드', 16000);
INSERT INTO product (id, name, price) VALUES (2, '양념치킨', 16000);

INSERT INTO menu (id, name, price, menu_group_id) VALUES (1, '후라이드치킨', 16000, 2);
INSERT INTO menu (id, name, price, menu_group_id) VALUES (2, '양념치킨', 16000, 2);

INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (1, 1, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (2, 2, 1);

INSERT INTO order_table (id, number_of_guests, empty) VALUES (1, 0, true);
INSERT INTO order_table (id, number_of_guests, empty) VALUES (2, 0, true);
