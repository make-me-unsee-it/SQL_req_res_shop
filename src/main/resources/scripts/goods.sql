create table goods
(
    id      int AUTO_INCREMENT,
    title   VARCHAR(50),
    price   DOUBLE,
    country VARCHAR(50)
);

insert into goods (title, price, country)
values ('iphone 10', 499.9, 'china');
insert into goods (title, price, country)
values ('samsung galaxy c5', 300.0, 'china');
insert into goods (title, price, country)
values ('google pixel 4', 250.0, 'taiwan');
insert into goods (title, price, country)
values ('nokia 1100', 15.0, 'finland');

create table users
(
    id          int AUTO_INCREMENT,
    userName    VARCHAR(50),
    password    VARCHAR(50)
);
insert into users (userName, password)
values ('Michail', 'qwerty');
insert into users (userName, password)
values ('Andrey', '12345');
insert into users (userName, password)
values ('Nikolay', 'xcvi12345');
insert into users (userName, password)
values ('Roma', '11111');

create table orders
(
    id          int AUTO_INCREMENT,
    userId      int,
    totalPrice  DOUBLE
);

create table ordergoods
(
    id          int AUTO_INCREMENT,
    orderId     int,
    goodId      int
);