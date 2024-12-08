create table inventory
(
    id           bigint auto_increment
        primary key,
    product_id   bigint           null,
    quantity     bigint default 0 null,
    restock_date timestamp        null,
    constraint inventory_ibfk_1
        foreign key (product_id) references product (id)
);

create index product_id
    on inventory (product_id);

