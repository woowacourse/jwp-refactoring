ALTER TABLE menu_product DROP CONSTRAINT fk_menu_product_product;
ALTER TABLE order_line_item DROP CONSTRAINT fk_order_line_item_menu;

ALTER TABLE menu_product ADD COLUMN product_name VARCHAR(255) NOT NULL DEFAULT 'NOT YET';
ALTER TABLE menu_product ADD COLUMN product_price DECIMAL(19, 2) NOT NULL DEFAULT 0;

UPDATE menu_product a
SET
    a.product_name = (SELECT b.name FROM product b WHERE b.id = a.product_id),
    a.product_price = (SELECT b.price FROM product b WHERE b.id = a.product_id)
WHERE exists(SELECT * FROM product b WHERE b.id = a.product_id);

ALTER TABLE order_line_item ADD COLUMN menu_name VARCHAR(255) NOT NULL DEFAULT 'NOT YET';
ALTER TABLE order_line_item ADD COLUMN menu_price DECIMAL(19, 2) NOT NULL DEFAULT 0;

UPDATE order_line_item a
SET
    a.menu_name = (SELECT b.name FROM menu b WHERE b.id = a.menu_id),
    a.menu_price = (SELECT b.price FROM menu b WHERE b.id = a.menu_id)
WHERE exists(SELECT * FROM menu b WHERE b.id = a.menu_id);
