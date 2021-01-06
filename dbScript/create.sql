drop database if exists `xiaosong`;
create database `xiaosong` default character set utf8;

drop table if exists `role`;
create table `role` (
    `id` int(11) not null auto_increment primary key,
    `role_name` varchar(255) not null unique,
    `description` varchar(255) default null
) ENGINE=InnoDB auto_increment=1 default charset=utf8;

drop table if exists `user`;
create table `user` (
    `id` int(11) not null auto_increment primary key,
    `username` varchar(255) not null,
    `password` varchar(255) not null,
    `enabled` tinyint(1) default 1,
    `locked` tinyint(1) default 0
) ENGINE=InnoDB auto_increment=1 default charset=utf8;

drop table if exists `user_role`;
create table `user_role` (
    `id` int(11) not null auto_increment primary key,
    `user_id` int(11) not null,
    `role_id` int(11) not null
) ENGINE=InnoDB auto_increment=1 default charset=utf8;

insert into role(role_name, description) values ('role_admin', '管理员角色');
insert into role(role_name, description) values ('role_dba', '数据库操作员角色');
insert into role(role_name, description) values ('role_user', '普通用户角色');
insert into role(role_name, description) values ('role_guest', '游客用户角色');

insert into user(username, password) values ('admin', '123456');
insert into user(username, password) values ('root', '123456');
insert into user(username, password) values ('xiaosong', '2.71828');

insert into user_role(user_id, role_id) values (1, 1);
insert into user_role(user_id, role_id) values (2, 2);
insert into user_role(user_id, role_id) values (3, 1);
insert into user_role(user_id, role_id) values (3, 2);
insert into user_role(user_id, role_id) values (3, 3);





