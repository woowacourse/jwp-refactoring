delete from menu_product;

ALTER TABLE menu_product
    ADD name varchar(255) not null;

ALTER TABLE menu_product
    ADD price decimal(19, 2) not null;

update menu_product mp
set (mp.name, mp.price) = (
    select p.name, p.price
    from product p
    where p.id = mp.product_id
    )
where exists (
    select 1
    from product p
    where p.id = mp.product_id
    );
