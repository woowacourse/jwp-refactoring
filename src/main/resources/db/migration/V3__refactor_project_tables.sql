ALTER TABLE table_group RENAME TO order_table_group;
ALTER TABLE menu_product RENAME COLUMN seq TO id;
ALTER TABLE order_line_item RENAME COLUMN seq TO id;