ALTER TABLE menu_product ALTER COLUMN menu_id SET NULL;
ALTER TABLE order_line_item DROP COLUMN menu_id;
ALTER TABLE order_line_item DROP COLUMN order_id;
ALTER TABLE order_line_item ADD COLUMN name varchar(255) not null;
ALTER TABLE order_line_item ADD COLUMN price DECIMAL(19, 2) NOT NULL;
ALTER TABLE table_group RENAME TO order_table_group;