create table orders
(
    order_id     bigint auto_increment
        primary key,
    customer_id  bigint                               not null,
    total_price  decimal(10, 2)                       not null,
    order_status tinyint(1) default 0                 null,
    created_at   timestamp  default CURRENT_TIMESTAMP null,
    updated_at   timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

