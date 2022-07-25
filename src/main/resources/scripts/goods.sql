create table goods
(
    id      int PRIMARY KEY AUTO_INCREMENT,
    title   VARCHAR(50),
    price   DOUBLE
);

insert into goods (title, price)
values ('iphone 10', 499.9);
insert into goods (title, price)
values ('samsung galaxy c5', 300.0);
insert into goods (title, price)
values ('google pixel 4', 250.0);
insert into goods (title, price)
values ('nokia 1100', 15.0);

create table users
(
    id          int PRIMARY KEY AUTO_INCREMENT,
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
    id          int PRIMARY KEY AUTO_INCREMENT,
    userId      int,
    totalPrice  DOUBLE,
    FOREIGN KEY (userId) REFERENCES users(id)
);

create table ordergoods
(
    id          int PRIMARY KEY AUTO_INCREMENT,
    orderId     int,
    goodId      int,
    FOREIGN KEY (orderId) REFERENCES orders(id),
    FOREIGN KEY (goodId) REFERENCES goods(id)
);