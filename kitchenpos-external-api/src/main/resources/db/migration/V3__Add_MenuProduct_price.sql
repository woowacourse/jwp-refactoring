ALTER TABLE menu_product
    ADD price DECIMAL(19, 2) NOT NULL DEFAULT 0;

UPDATE menu_product mp
SET mp.price = (SELECT price FROM product p where p.id = mp.product_id);
