/* change order_table's 'empty' column to 'orderable' */
ALTER TABLE order_table RENAME COLUMN `empty` TO orderable;