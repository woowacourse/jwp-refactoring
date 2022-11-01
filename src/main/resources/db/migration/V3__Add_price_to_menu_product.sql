ALTER TABLE menu_product
    ADD COLUMN price DECIMAL(19, 2);

INSERT INTO menu_product (price)
SELECT (p.price * quantity)
FROM menu_product mp
         INNER JOIN product p ON mp.product_id = p.id
WHERE mp.price = null
