alter table menu_product add column product_price_snapshot decimal(19, 2);

update menu_product mp
set mp.product_price_snapshot = (select p.price from product p where mp.product_id = p.id);

alter table menu_product alter column product_price_snapshot set not null;
