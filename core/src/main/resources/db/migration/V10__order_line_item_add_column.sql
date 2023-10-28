ALTER TABLE order_line_item
    ADD name varchar(255) not null;
ALTER TABLE order_line_item
    ADD price decimal(19, 2) not null;
