create table catalog (
    id int primary key auto_increment,
    product_id varchar(120) unique not null,
    product_name varchar not null,
    stock int not null,
    unit_price int not null,
    created_at datetime not null default now()
);