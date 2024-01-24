create table customer
(
    customerId   varchar(40) not null
        primary key,
    customerName varchar(50) null,
    city         varchar(15) null,
    email        varchar(50) null
);
////////////////////////////////////////////
create table item
(
    itemCode    varchar(10)  not null
        primary key,
    description varchar(100) null,
    qty         varchar(10)  null,
    price       varchar(10)  null
);
/////////////////////////////////////////
create table orderdetails
(
    order_id varchar(50) not null,
    item_id  varchar(50) not null,
    price    double      null,
    qty      int         null,
    primary key (order_id, item_id),
    constraint orderdetails_ibfk_1
        foreign key (order_id) references orders (order_id),
    constraint orderdetails_ibfk_2
        foreign key (item_id) references item (itemCode)
);

create index item_id
    on orderdetails (item_id);

//////////////////////////////////////////
create table orders
(
    order_date  date        null,
    order_id    varchar(50) not null
        primary key,
    customer_id varchar(50) null,
    total       double      null,
    discount    double      null,
    cash        double      null,
    constraint orders_ibfk_1
        foreign key (customer_id) references customer (customerId)
);

create index customer_id
    on orders (customer_id);


