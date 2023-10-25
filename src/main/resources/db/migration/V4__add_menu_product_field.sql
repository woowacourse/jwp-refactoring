ALTER TABLE menu_product
    ADD COLUMN price DECIMAL(19, 2) DEFAULT NULL;

ALTER TABLE menu_product
    ADD COLUMN name VARCHAR(255) DEFAULT NULL;

UPDATE menu_product
SET price = (SELECT product.price FROM product WHERE product.id = menu_product.product_id),
    name  = (SELECT product.name FROM product WHERE product.id = menu_product.product_id);

