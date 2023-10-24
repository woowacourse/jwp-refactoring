alter table menu_product add price decimal(19, 2);
alter table menu_product add name varchar(255);

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
