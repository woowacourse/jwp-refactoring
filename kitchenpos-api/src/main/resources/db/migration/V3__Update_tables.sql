alter table menu_product
    add name varchar(255);

alter table menu_product
    add price decimal(19, 2);

UPDATE menu_product mp
SET (mp.name, mp.price) = (
    SELECT p.name, p.price
    FROM product p
    WHERE p.id = mp.product_id
)
WHERE EXISTS (
    SELECT 1
    FROM product p
    WHERE p.id = mp.product_id
);

alter table menu_product
    drop foreign key fk_menu_product_to_product;

alter table menu_product
    drop product_id;

alter table menu_product
alter column name varchar(255) not null;

alter table menu_product
alter column price decimal(19, 2) not null;


alter table order_line_item
    add menu_name varchar(255);

alter table order_line_item
    add menu_price decimal(19, 2);

UPDATE order_line_item oli
SET (oli.menu_name, oli.menu_price) = (
    SELECT m.name, m.price
    FROM menu m
    WHERE m.id = oli.menu_id
)
WHERE EXISTS (
    SELECT 1
    FROM menu m
    WHERE m.id = oli.menu_id
);

alter table order_line_item
drop menu_id;
