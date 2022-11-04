CREATE TABLE order_status_record
(
    order_id       BIGINT(20) NOT NULL,
    order_table_id BIGINT(20) NOT NULL,
    order_status   VARCHAR(255) NOT NULL,
    PRIMARY KEY (order_id)
);

ALTER TABLE order_status_record
    ADD CONSTRAINT fk_order_status_record_order_table
        FOREIGN KEY (order_table_id) REFERENCES order_table (id);
