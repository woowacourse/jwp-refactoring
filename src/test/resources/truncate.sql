SET
FOREIGN_KEY_CHECKS = 0;

truncate table orders;
alter table orders
    alter column id restart with 1;

truncate table order_line_item;
alter table order_line_item
    alter column seq restart with 1;

truncate table menu;
alter table menu
    alter column id restart with 1;

truncate table menu_group;
alter table menu_group
    alter column id restart with 1;

truncate table menu_product;
alter table menu_product
    alter column seq restart with 1;

truncate table order_table;
alter table order_table
    alter column id restart with 1;

truncate table table_group;
alter table table_group
    alter column id restart with 1;

truncate table product;
alter table product
    alter column id restart with 1;

SET
FOREIGN_KEY_CHECKS = 1;
