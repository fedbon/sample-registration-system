create table if not exists refresh_tokens
(
    id         bigserial primary key,
    created_at timestamp(6) with time zone,
    token      varchar(255)
);

create table users
(
    user_id  serial primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(255)
);

create table roles
(
    role_id serial primary key,
    name    varchar(255) not null unique
);

create table user_roles
(
    user_id int references users (user_id),
    role_id int references roles (role_id),
    primary key (user_id, role_id)
);

create table if not exists user_requests
(
    request_id   bigserial primary key,
    status       varchar(255),
    request_text varchar(1500),
    created      timestamp(6) with time zone,
    user_id      bigint references users (user_id)
);

