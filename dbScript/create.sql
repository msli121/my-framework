drop database if exists `xiaosong`;
create database `xiaosong` default character set utf8;
use xiaosong;
drop table if exists `t_role`;
create table `t_role` (
    `id` int(11) not null auto_increment primary key,
    `role_name` varchar(255) not null unique,
    `description` varchar(255) default null
) ENGINE=InnoDB auto_increment=1 default charset=utf8;

drop table if exists `t_user`;
create table `t_user` (
    `id` int(11) not null auto_increment primary key,
    `username` varchar(255) not null,
    `password` varchar(255) not null,
    `salt` varchar(255) default '',
    `enabled` tinyint(1) default 1,
    `locked` tinyint(1) default 0
) ENGINE=InnoDB auto_increment=1 default charset=utf8;

drop table if exists `t_user_role`;
create table `t_user_role` (
    `id` int(11) not null auto_increment primary key,
    `user_id` int(11) not null,
    `role_id` int(11) not null
) ENGINE=InnoDB auto_increment=1 default charset=utf8;

drop table if exists `t_menu`;
create table `t_menu` (
    `id` int(11) not null auto_increment primary key,
    `path` varchar(255) default '',
    `name` varchar(255) default '',
    `name_zh` varchar(255) default '',
    `icon` varchar(255) default '',
    `component` varchar(255) default '',
    `parent_id` int(11)
)  ENGINE=InnoDB auto_increment=1 default charset=utf8;

drop table if exists `t_role_menu`;
create table `t_role_menu` (
    `id` int(11) not null auto_increment primary key,
    `role_id` int(11) not null,
    `menu_id` int(11) not null
) ENGINE=InnoDB auto_increment=1 default charset=utf8;

insert into t_role(role_name, description) values ('role_admin', '管理员角色');
insert into t_role(role_name, description) values ('role_dba', '数据库操作员角色');
insert into t_role(role_name, description) values ('role_user', '普通用户角色');
insert into t_role(role_name, description) values ('role_guest', '游客用户角色');

insert into t_user(username, password) values ('admin', '123456');
insert into t_user(username, password) values ('root', '123456');
insert into t_user(username, password) values ('xiaosong', '2.71828');

insert into t_user_role(user_id, role_id) values (1, 1);
insert into t_user_role(user_id, role_id) values (2, 2);
insert into t_user_role(user_id, role_id) values (3, 1);
insert into t_user_role(user_id, role_id) values (3, 2);
insert into t_user_role(user_id, role_id) values (3, 3);

insert into t_menu(path, name, name_zh, icon, component, parent_id) values ('/admin', 'admin', '用户管理', 'el-icon-user', 'adminIndex', 0);
insert into t_menu(path, name, name_zh, icon, component, parent_id) values ('/admin/user/basic', null, '用户信息', null, 'userBasic', 1);
insert into t_menu(path, name, name_zh, icon, component, parent_id) values ('/admin/user/role', null, '角色配置', null, 'userRole', 1);
insert into t_menu(path, name, name_zh, icon, component, parent_id) values ('/admin', 'config', '系统配置', 'el-icon-s-tools', 'adminIndex', 0);








