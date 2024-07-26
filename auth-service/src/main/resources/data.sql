/*user:password*/
insert into users(username, password) values ('user', '$2y$04$gMllrpB0tMcOAYi22jkI1eIIbR/jwNlQOhexTlZ9D90VXwt51A4D.');
/*admin:pass*/
insert into users(username, password) values ('admin', '$2y$04$A./uSTFJZdu5NLefAGuE8.N9ZdNDrEH/4AAcOrhvz4fTKLRWwPXOW');

insert into user_user_roles(user_id, user_roles) values (select id from users where username = 'user', 'ROLE_USER');
insert into user_user_roles(user_id, user_roles) values (select id from users where username = 'admin', 'ROLE_ADMIN');
insert into user_user_roles(user_id, user_roles) values (select id from users where username = 'admin', 'ROLE_USER');