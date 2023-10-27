alter table menu_product
    add column name varchar(255);

alter table menu_product
    add column price decimal(19, 2);

update menu_product
set price = (select p.price from product as p where p.id = product_id),
    name  = (select p.name from product as p where p.id = product_id);

alter table menu_product
    alter column name set not null;

alter table menu_product
    alter column price set not null;
