CREATE TABLE order_detail (
                      id BIGINT(20) NOT NULL AUTO_INCREMENT,
                      order_id BIGINT(20) NOT NULL,
                      order_table_id BIGINT(20) NOT NULL,
                      PRIMARY KEY (id)
);

ALTER TABLE order_detail
    ADD CONSTRAINT fk_order_detail_order
        FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_detail
    ADD CONSTRAINT fk_order_detail_order_table
        FOREIGN KEY (order_table_id) REFERENCES order_table (id);
