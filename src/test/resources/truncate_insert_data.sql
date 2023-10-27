SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE menu RESTART IDENTITY;
TRUNCATE TABLE menu_group RESTART IDENTITY;
TRUNCATE TABLE menu_product RESTART IDENTITY;
TRUNCATE TABLE order_line_item RESTART IDENTITY;
TRUNCATE TABLE orders RESTART IDENTITY;
TRUNCATE TABLE order_table RESTART IDENTITY;
TRUNCATE TABLE product RESTART IDENTITY;
TRUNCATE TABLE table_group RESTART IDENTITY;
SET REFERENTIAL_INTEGRITY TRUE;

insert into product (name, price)
values ('후라이드', 16000);
insert into product (name, price)
values ('양념치킨', 16000);
insert into product (name, price)
values ('반반치킨', 16000);
insert into product (name, price)
values ('통구이', 16000);
insert into product (name, price)
values ('간장치킨', 17000);
insert into product (name, price)
values ('순살치킨', 17000);

insert into menu_group (name)
values ('두마리메뉴');
insert into menu_group (name)
values ('한마리메뉴');
insert into menu_group (name)
values ('순살파닭두마리메뉴');
insert into menu_group (name)
values ('신메뉴');

insert into menu (name, price, menu_group_id)
values ('후라이드치킨', 16000, 2);
insert into menu (name, price, menu_group_id)
values ('양념치킨', 16000, 2);
insert into menu (name, price, menu_group_id)
values ('반반치킨', 16000, 2);
insert into menu (name, price, menu_group_id)
values ('통구이', 16000, 2);
insert into menu (name, price, menu_group_id)
values ('간장치킨', 17000, 2);
insert into menu (name, price, menu_group_id)
values ('순살치킨', 17000, 2);

insert into menu_product (menu_id, product_id, quantity)
values (1, 1, 1);
insert into menu_product (menu_id, product_id, quantity)
values (2, 2, 1);
insert into menu_product (menu_id, product_id, quantity)
values (3, 3, 1);
insert into menu_product (menu_id, product_id, quantity)
values (4, 4, 1);
insert into menu_product (menu_id, product_id, quantity)
values (5, 5, 1);
insert into menu_product (menu_id, product_id, quantity)
values (6, 6, 1);

insert into order_table (number_of_guests, empty)
values (0, true);
insert into order_table (number_of_guests, empty)
values (0, true);
insert into order_table (number_of_guests, empty)
values (0, true);
insert into order_table (number_of_guests, empty)
values (0, true);
insert into order_table (number_of_guests, empty)
values (0, true);
insert into order_table (number_of_guests, empty)
values (0, true);
insert into order_table (number_of_guests, empty)
values (0, true);
insert into order_table (number_of_guests, empty)
values (0, true);
