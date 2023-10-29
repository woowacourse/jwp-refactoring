alter table menu_product
    drop column product_id;

alter table menu_product
    add column price decimal(19, 2) not null;

alter table menu_product
    add column name VARCHAR(255) not null;
