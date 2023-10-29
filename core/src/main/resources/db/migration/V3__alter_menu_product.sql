alter table menu_product add price decimal(19, 2) not null default 0;

update menu_product mp
set mp.price = (
    select p.price
    from product p
    where p.id = mp.product_id
);
