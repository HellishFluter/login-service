create sequence hibernate_sequence start 1 increment 1;

create table roles
(
    id   int4        not null,
    name varchar(36) not null,
    primary key (id)
);

create table users
(
    id                 bigserial not null,
    created_date       timestamp default (now() at time zone 'utc'),
    modified_date      timestamp default (now() at time zone 'utc'),
    first_name     varchar(255)  not null,
    last_name     varchar(255)  not null,
    middle_name     varchar(255),
    login     varchar(255) UNIQUE not null,
    password      varchar(255)        not null,
    role_id int4 not null,
    primary key (id),
    constraint fk_role foreign key (role_id) references roles (id)
);

insert into roles (id, name)
VALUES (1, 'Admin'),
       (2, 'User');

insert into users (first_name, last_name, middle_name, login, password, role_id)
VALUES ('John', 'Smith', null, 'admin', '$2a$10$YD945wlh5kdymtdZaLBo8eSst/JZxo4TFdqlta8n2w36Cic0ckdbu', 1);







