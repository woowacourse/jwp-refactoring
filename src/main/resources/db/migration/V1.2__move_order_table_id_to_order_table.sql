alter table orders drop column order_table_id;
alter table order_table add column order_id BIGINT(20);

alter table order_table add foreign key(order_id) references orders(id);
