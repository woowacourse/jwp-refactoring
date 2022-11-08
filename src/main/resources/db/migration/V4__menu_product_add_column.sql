ALTER TABLE menu_product ADD price DECIMAL(19, 2);

UPDATE menu_product m SET m.price = (SELECT p.price from product p where p.id = m.product_id);

ALTER TABLE menu_product ALTER COLUMN price DECIMAL(19, 2) NOT NULL;
