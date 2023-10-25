delete from menu_product;

ALTER TABLE menu_product
    ADD name varchar(255) not null;

ALTER TABLE menu_product
    ADD price decimal(19, 2) not null;

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

insert into menu_product (menu_id, product_id, name, price, quantity)
values (1, 1, '후라이드', 16000, 1);
insert into menu_product (menu_id, product_id, name, price, quantity)
values (2, 2, '양념치킨', 16000, 1);
insert into menu_product (menu_id, product_id, name, price, quantity)
values (3, 3, '반반치킨', 16000, 1);
insert into menu_product (menu_id, product_id, name, price, quantity)
values (4, 4, '통구이', 16000, 1);
insert into menu_product (menu_id, product_id, name, price, quantity)
values (5, 5, '간장치킨', 17000, 1);
insert into menu_product (menu_id, product_id, name, price, quantity)
values (6,  6, '순살치킨', 17000, 1);
