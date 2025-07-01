create table category
(
    id          bigint auto_increment
        primary key,
    created_at  datetime(6)                         null,
    updated_at  timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    description text                                not null,
    name        varchar(50)                         not null,
    constraint UK46ccwnsi9409t36lurvtyljak
        unique (name)
);

create table product
(
    id          bigint auto_increment
        primary key,
    created_at  datetime(6)                         null,
    updated_at  timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    description varchar(100)                        not null,
    name        varchar(100)                        not null,
    price       decimal(10, 2)                      not null,
    category_id bigint                              not null,
    constraint FK1mtsbur82frn64de7balymq9s
        foreign key (category_id) references category (id)
);

create table role
(
    name varchar(50) not null,
    id   bigint auto_increment
        primary key,
    constraint UK8sewwnpamngi6b1dwaa88askk
        unique (name),
    check (`name` between 0 and 3)
);

INSERT INTO role (name) VALUES
                            ('STAFF'),
                            ('ADMIN'),
                            ('CUSTOMER');


create table user
(
    id            bigint auto_increment
        primary key,
    created_at    datetime(6)                                         null,
    updated_at    timestamp default CURRENT_TIMESTAMP                 null on update CURRENT_TIMESTAMP,
    date_of_birth date                                                null,
    email         varchar(255)                                        not null,
    full_name     varchar(100) charset utf8mb3                        not null,
    gender        enum ('FEMALE', 'MALE', 'OTHER')                    null,
    password      varchar(255)                                        not null,
    phone         varchar(20)                                         not null,
    status        enum ('ACTIVE', 'DELETED', 'INACTIVE', 'IS_BANNED') null,
    username      varchar(100)                                        not null,
    role_id       bigint                                              not null,
    constraint UK589idila9li6a4arw1t8ht1gx
        unique (phone),
    constraint UKob8kqyqqgmefl0aco34akdtpe
        unique (email),
    constraint UKsb8bbouer5wak8vyiiy4pf2bx
        unique (username),
    constraint FKn82ha3ccdebhokx3a8fgdqeyy
        foreign key (role_id) references role (id)
);

create table address
(
    id               bigint auto_increment
        primary key,
    created_at       datetime(6)                         null,
    updated_at       timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    address_type     int                                 null,
    apartment_number varchar(50)                         null,
    building         varchar(50)                         null,
    city             varchar(50)                         not null,
    country          varchar(255)                        not null,
    district         varchar(50)                         not null,
    floor            varchar(50)                         null,
    street           varchar(50)                         not null,
    street_number    varchar(50)                         not null,
    ward             varchar(50)                         null,
    user_id          bigint                              not null,
    constraint FKda8tuywtf0gb6sedwk7la1pgi
        foreign key (user_id) references user (id)
);

create table cart
(
    id          bigint auto_increment
        primary key,
    created_at  datetime(6)                         null,
    updated_at  timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    total_price decimal(10, 2)                      not null,
    user_id     bigint                              not null,
    constraint UK9emlp6m95v5er2bcqkjsw48he
        unique (user_id),
    constraint FKl70asp4l4w0jmbm1tqyofho4o
        foreign key (user_id) references user (id)
);

create table `order`
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)                         null,
    updated_at timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    user_id    bigint                              not null,
    constraint FKcpl0mjoeqhxvgeeeq5piwpd3i
        foreign key (user_id) references user (id)
);

ALTER TABLE user DROP CONSTRAINT FKn82ha3ccdebhokx3a8fgdqeyy;
DROP TABLE IF EXISTS role;

CREATE TABLE role (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE,
                      CHECK (name IN ('ADMIN', 'CUSTOMER', 'STAFF', 'GUEST'))
);

INSERT INTO role (name) VALUES
                            ('STAFF'),
                            ('ADMIN'),
                            ('CUSTOMER'),
                            ('GUEST');

SELECT * FROM role;

ALTER TABLE user ADD CONSTRAINT FKn82ha3ccdebhokx3a8fgdqeyy FOREIGN KEY (role_id) REFERENCES role (id);