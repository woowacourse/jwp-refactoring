ALTER TABLE order_line_item
    ADD COLUMN name varchar(255) not null;
ALTER TABLE order_line_item
    ADD COLUMN price decimal(19, 2) not null;
