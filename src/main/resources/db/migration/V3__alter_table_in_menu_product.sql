ALTER TABLE menu_product ADD product_name VARCHAR(255);
ALTER TABLE menu_product ADD product_price DECIMAL(19, 2);

UPDATE menu_product SET product_name = '후라이드치킨', product_price = 16000 WHERE product_id = 1;
UPDATE menu_product SET product_name = '양념치킨', product_price = 16000 WHERE product_id = 2;
UPDATE menu_product SET product_name = '반반치킨', product_price = 16000 WHERE product_id = 3;
UPDATE menu_product SET product_name = '통구이', product_price = 16000 WHERE product_id = 4;
UPDATE menu_product SET product_name = '간장치킨', product_price = 17000 WHERE product_id = 5;
UPDATE menu_product SET product_name = '순살치킨', product_price = 17000 WHERE product_id = 6;

ALTER TABLE menu_product ALTER COLUMN product_name VARCHAR(255) NOT NULL;
ALTER TABLE menu_product ALTER COLUMN product_price DECIMAL(19, 2) NOT NULL;

ALTER TABLE menu_product ALTER COLUMN seq BIGINT(20) NOT NULL;
ALTER TABLE menu_product DROP PRIMARY KEY;
ALTER TABLE menu_product ADD PRIMARY KEY(product_id, product_name, product_price, quantity);
