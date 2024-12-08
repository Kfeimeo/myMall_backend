create table product
(
    id          bigint auto_increment
        primary key,
    name        varchar(255)                         not null,
    description text                                 null,
    price       decimal(10, 2)                       not null,
    category_id bigint                               null,
    brand_id    bigint                               null,
    stock       bigint     default 0                 null,
    is_active   tinyint(1) default 1                 null,
    created_at  timestamp  default CURRENT_TIMESTAMP null,
    updated_at  timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    image_url   varchar(255)                         not null,
    constraint product_ibfk_1
        foreign key (category_id) references categories (id),
    constraint product_ibfk_2
        foreign key (brand_id) references brands (id)
);

create index brand_id
    on product (brand_id);

create index category_id
    on product (category_id);

