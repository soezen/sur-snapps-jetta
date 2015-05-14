delete from users
where id = 'test'
and name like 'this';
delete from entities
where user_id in (
  select id from users
);
delete from tokens
where id = 'this';