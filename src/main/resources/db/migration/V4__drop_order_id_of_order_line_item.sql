ALTER TABLE order_line_item DROP CONSTRAINT fk_order_line_item_to_orders;

ALTER TABLE order_line_item DROP COLUMN order_id;
