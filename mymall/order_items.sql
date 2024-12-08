create table order_items
(
    order_item_id bigint auto_increment
        primary key,
    order_id      bigint not null,
    product_id    bigint not null,
    quantity      int    not null,
    constraint order_items_ibfk_1
        foreign key (order_id) references orders (order_id)
            on delete cascade,
    constraint order_items_ibfk_2
        foreign key (product_id) references product (id)
            on delete cascade
);

create index order_id
    on order_items (order_id);

create index product_id
    on order_items (product_id);

