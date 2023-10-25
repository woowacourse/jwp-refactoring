alter table menu_product add column price decimal(19, 2) default null;

alter table menu_product add column name varchar(255) default null;

update menu_product set name = (select name from product where id = menu_product.product_id);

update menu_product set price = (select price from product where id = menu_product.product_id);

alter table menu_product drop column product_id;
