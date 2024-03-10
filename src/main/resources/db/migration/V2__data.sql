insert into users (username, password)
values ('Max_Payne', '$2a$12$y83j0dKpzH0ilF5ghrjOreg/CasWdwXMH.5PofwUGgnwlHZaeELlG'),
       ('John_Shepard', '$2a$12$y83j0dKpzH0ilF5ghrjOreg/CasWdwXMH.5PofwUGgnwlHZaeELlG'),
       ('Gordon_Freeman', '$2a$12$y83j0dKpzH0ilF5ghrjOreg/CasWdwXMH.5PofwUGgnwlHZaeELlG'),
       ('Jack_London', '$2a$12$y83j0dKpzH0ilF5ghrjOreg/CasWdwXMH.5PofwUGgnwlHZaeELlG');

insert into roles (name)
values ('ADMIN'),
       ('USER'),
       ('OPERATOR');

insert into user_roles (user_id, role_id)
values (1, 1),
       (2, 2),
       (3, 2),
       (4, 3);
