CREATE TABLE order_menu_product
(
    seq           BIGINT(20) NOT NULL AUTO_INCREMENT,
    order_menu_id BIGINT(20),
    order_product_name  VARCHAR(255)   NOT NULL,
    order_product_price DECIMAL(19, 2) NOT NULL,
    quantity      BIGINT(20) NOT NULL,
    PRIMARY KEY (seq)
);
