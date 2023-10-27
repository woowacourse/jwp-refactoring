ALTER TABLE menu_product
    rename column seq to id;

ALTER TABLE order_line_item
    rename column seq to id;

ALTER TABLE menu_product
    ALTER COLUMN quantity SET DATA TYPE integer;

ALTER TABLE order_line_item
    ALTER COLUMN quantity SET DATA TYPE integer;
