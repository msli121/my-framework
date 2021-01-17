drop database if exists `xiaosong`;
create database `xiaosong` default character set utf8;
use xiaosong;
drop table if exists `t_role`;
create table `t_role` (
    `id` int(11) not null auto_increment primary key,
    `role_code` varchar(255) not null unique,
    `role_name` varchar(255) not null,
    `description` varchar(255) default null
) ENGINE=InnoDB auto_increment=1 default charset=utf8;

drop table if exists `t_user`;
create table `t_user` (
    `id` int(11) not null auto_increment primary key,
    `username` varchar(255) not null,
    `password` varchar(255) not null,
    `phone` varchar(50) default '',
    `salt` varchar(255) default '',
    `enabled` tinyint(1) default 1,
    `locked` tinyint(1) default 0
) ENGINE=InnoDB auto_increment=1 default charset=utf8;

drop table if exists `t_user_role`;
create table `t_user_role`(
    `id` int(11) not null auto_increment primary key,
    `user_id` int(11) not null,
    `role_code` varchar(255) default '',
    `description` varchar(255) default null
)ENGINE=InnoDB auto_increment=1 default charset=utf8;

drop table if exists `t_menu`;
create table `t_menu` (
    `id` int(11) not null auto_increment primary key,
    `menu_code` varchar(255) not null unique,
    `path` varchar(255) default null,
    `name` varchar(255) default null,
    `name_zh` varchar(255) default null,
    `icon` varchar(255) default null,
    `component` varchar(255) default null,
    `parent_menu_code` varchar(255) default null,
    `description` varchar(255) default null
)  ENGINE=InnoDB auto_increment=1 default charset=utf8;

drop table if exists `t_role_menu`;
create table `t_role_menu` (
    `id` int(11) not null auto_increment primary key,
    `role_code` varchar(255) default '',
    `menu_code` varchar(255) default '',
    `description` varchar(255) default null
) ENGINE=InnoDB auto_increment=1 default charset=utf8;

# 系统默认角色
insert into t_role(role_name, role_code, description) values ('role_admin', 'ROLE_ADMIN', '管理员角色');
insert into t_role(role_name, role_code, description) values ('role_dba', 'ROLE_DBA', '数据库操作员角色');
insert into t_role(role_name, role_code, description) values ('role_user', 'ROLE_NORMAL', '普通用户角色');
insert into t_role(role_name, role_code, description) values ('role_guest', 'ROLE_GUEST', '游客用户角色');

# 系统菜单路由
insert into t_menu(menu_code, path, name, name_zh, icon, component, parent_menu_code) values ('MENU_ADMIN_USER', '/admin', 'admin', '用户管理', 'el-icon-user', 'adminIndex', null);
insert into t_menu(menu_code, path, name, name_zh, icon, component, parent_menu_code) values ('MENU_ADMIN_USER_BASIC',  '/admin/user/basic', 'adminUserBasic', '用户信息', null, 'adminUserBasic', 'MENU_ADMIN_USER');
insert into t_menu(menu_code, path, name, name_zh, icon, component, parent_menu_code) values ('MENU_ADMIN_USER_ROLE', '/admin/user/role', 'adminUserRole', '角色配置', null, 'adminUserRole', 'MENU_ADMIN_USER');

insert into t_menu(menu_code, path, name, name_zh, icon, component, parent_menu_code) values ('MENU_ADMIN_CONFIG', '/admin', 'config', '系统配置', 'el-icon-s-tools', 'adminIndex', null);

insert into t_menu(menu_code, path, name, name_zh, icon, component, parent_menu_code) values ('MENU_ADMIN_STATISTIC', '/admin', 'statistic', '数据统计', 'el-icon-s-tools', 'adminIndex', null);
insert into t_menu(menu_code, path, name, name_zh, icon, component, parent_menu_code) values ('MENU_ADMIN_STATISTIC_OCR', '/admin/statistic/ocr', 'statistic_ocr', 'ocr', null, 'adminStatisticOcr', 'MENU_ADMIN_STATISTIC');
insert into t_menu(menu_code, path, name, name_zh, icon, component, parent_menu_code) values ('MENU_ADMIN_STATISTIC_PDF', '/admin/statistic/pdf', 'statistic_pdf', 'pdf', null, 'adminStatisticPdf', 'MENU_ADMIN_STATISTIC');


# 添加角色和菜单映射表 测试使用
# admin 角色
insert into t_role_menu(role_code, menu_code) values ('ROLE_ADMIN', 'MENU_ADMIN_USER');
insert into t_role_menu(role_code, menu_code) values ('ROLE_ADMIN', 'MENU_ADMIN_USER_BASIC');
insert into t_role_menu(role_code, menu_code) values ('ROLE_ADMIN', 'MENU_ADMIN_USER_ROLE');
insert into t_role_menu(role_code, menu_code) values ('ROLE_ADMIN', 'MENU_ADMIN_CONFIG');
insert into t_role_menu(role_code, menu_code) values ('ROLE_ADMIN', 'MENU_ADMIN_STATISTIC');
insert into t_role_menu(role_code, menu_code) values ('ROLE_ADMIN', 'MENU_ADMIN_STATISTIC_PDF');
insert into t_role_menu(role_code, menu_code) values ('ROLE_ADMIN', 'MENU_ADMIN_STATISTIC_OCR');

# normal 角色
insert into t_role_menu(role_code, menu_code) values ('ROLE_NORMAL', 'MENU_ADMIN_USER');
insert into t_role_menu(role_code, menu_code) values ('ROLE_NORMAL', 'MENU_ADMIN_USER_BASIC');
insert into t_role_menu(role_code, menu_code) values ('ROLE_NORMAL', 'MENU_ADMIN_USER_ROLE');
insert into t_role_menu(role_code, menu_code) values ('ROLE_NORMAL', 'MENU_ADMIN_CONFIG');

# 添加admin/xiaosong用户 测试使用
insert into t_user_role(user_id, role_code, description) values (1, 'ROLE_ADMIN', '拥有ROLE_ADMIN角色，测试使用');
insert into t_user_role(user_id, role_code, description) values (2, 'ROLE_NORMAL', '拥有ROLE_NORMAL，测试使用');

# 添加头像信息
ALTER TABLE t_user ADD sys_header_icon varchar(255) CHARACTER SET utf8  DEFAULT 'head-profile-1.jpg' AFTER salt;
# 添加邮箱信息
ALTER TABLE t_user ADD email varchar(255) CHARACTER SET utf8  DEFAULT '' AFTER phone;

# 数据权限
# 新增权限表
drop table if exists `t_permission`;
create table `t_permission` (
    `id` int not null auto_increment primary key,
    `group_code` varchar(255) default '',
    `permission_code` varchar(255) default '' unique,
    `url` varchar(255) default '',
    `description` varchar(255) default ''
)ENGINE=InnoDB auto_increment=1 default charset=utf8;
# 添加测试数据
insert into t_permission(group_code, permission_code, url, description) values ('admin', 'PERMISSION_ADMIN_ALL_USERS', '/api/admin/users', '');
insert into t_permission(group_code, permission_code, url, description) values ('admin', 'PERMISSION_ADMIN_CURRENT_ROLES', '/api/admin/roles', '');
insert into t_permission(group_code, permission_code, url, description) values ('admin', 'PERMISSION_ADMIN_ALL_ROLES', '/api/admin/roles/all', '');
insert into t_permission(group_code, permission_code, url, description) values ('admin', 'PERMISSION_ADMIN_CURRENT_MENUS', '/api/admin/menus', '');
insert into t_permission(group_code, permission_code, url, description) values ('admin', 'PERMISSION_ADMIN_ALL_MENUS', '/api/admin/menus/all', '');


# 新增角色与权限关联表
drop table if exists `t_role_permission`;
create table `t_role_permission` (
    `id` int(11) not null auto_increment primary key,
    `role_code` varchar(255) default '',
    `permission_code` varchar(255) default '',
    `description` varchar(255) default ''
)

# 添加测试数据
insert into t_role_permission(role_code, permission_code, description)
    values ('ROLE_ADMIN', 'PERMISSION_ADMIN_ALL_USERS', ''),
           ('ROLE_ADMIN', 'PERMISSION_ADMIN_CURRENT_ROLES', ''),
           ('ROLE_ADMIN', 'PERMISSION_ADMIN_ALL_ROLES', ''),
           ('ROLE_ADMIN', 'PERMISSION_ADMIN_CURRENT_MENUS', ''),
           ('ROLE_ADMIN', 'PERMISSION_ADMIN_ALL_MENUS', '');
insert into t_role_permission(role_code, permission_code, description)
    values ('ROLE_NORMAL', 'PERMISSION_ADMIN_CURRENT_ROLES', ''),
           ('ROLE_NORMAL', 'PERMISSION_ADMIN_CURRENT_MENUS', '');






