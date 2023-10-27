create table order_menu
(
    id   bigint       not null auto_increment,
    menu_id bigint not null,
    name varchar(255) not null,
    price decimal(19, 2) not null,
    primary key (id)
);
