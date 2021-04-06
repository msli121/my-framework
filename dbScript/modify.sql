use ocr;
alter table t_user add column`registry_time` timestamp null
    comment '注册时间' after username;