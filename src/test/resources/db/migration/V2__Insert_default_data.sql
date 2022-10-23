INSERT INTO product (name, price) VALUES ('맘모스빵', 3000);
INSERT INTO product (name, price) VALUES ('팥빵', 1000);
INSERT INTO product (name, price) VALUES ('소보로빵', 2000);

INSERT INTO menu_group (name) VALUES ('베이커리');
INSERT INTO menu_group (name) VALUES ('한마리메뉴');

INSERT INTO order_table (id, number_of_guests, empty) VALUES (1, 0, false);
INSERT INTO order_table (id, number_of_guests, empty) VALUES (2, 0, true);

INSERT INTO menu (id, name, price, menu_group_id) VALUES (1, '후라이드치킨', 16000, 2);
INSERT INTO menu (id, name, price, menu_group_id) VALUES (2, '양념치킨', 16000, 2);
INSERT INTO menu (id, name, price, menu_group_id) VALUES (3, '반반치킨', 16000, 2);
