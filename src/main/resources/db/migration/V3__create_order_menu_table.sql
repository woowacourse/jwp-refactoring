CREATE TABLE order_menu
(
    id              BIGINT(20) NOT NULL AUTO_INCREMENT,
    order_id        BIGINT(20),
    menu_name       VARCHAR(255)   NOT NULL,
    menu_price      DECIMAL(19, 2) NOT NULL,
    menu_group_name VARCHAR(255)   NOT NULL,
    PRIMARY KEY (id)
);
