ALTER TABLE order_line_item
    ADD COLUMN name varchar(255) DEFAULT NULL;

ALTER TABLE order_line_item
    ADD COLUMN price DECIMAL(19, 2) DEFAULT NULL;

create table order_menu_product
(
    order_line_item_seq bigint         not null,
    price               decimal(19, 2) not null,
    quantity            bigint         not null,
    name                varchar(255)   not null
);
