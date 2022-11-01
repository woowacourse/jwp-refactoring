ALTER TABLE menu_product ADD COLUMN `price` DECIMAL(19, 2);

UPDATE menu_product SET price = 16000 WHERE seq = 1;
UPDATE menu_product SET price = 16000 WHERE seq = 2;
UPDATE menu_product SET price = 16000 WHERE seq = 3;
UPDATE menu_product SET price = 16000 WHERE seq = 4;
UPDATE menu_product SET price = 17000 WHERE seq = 5;
UPDATE menu_product SET price = 17000 WHERE seq = 6;

-- H2 DB 의존적
ALTER TABLE menu_product ALTER `price` SET NOT NULL;
