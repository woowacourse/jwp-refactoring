insert into menu_group (name) values ('메뉴그룹');

insert into product (name, price) values ('상품', 10000);

insert into menu (name, price, menu_group_id) values ('메뉴', 10000, 1);

insert into menu_product (menu_id, product_id, quantity, name, price) values (1, 1, 1, '상품', 10000);

insert into table_group (created_date) values (CURRENT_DATE);
