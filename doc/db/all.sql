--大章

drop table if exists `chapter`;
drop table if exists `chaper`;
create table `chapter`(
    `id` char(8) not null comment 'ID',
    `course_id` char(8) comment '课程ID',
    `name` varchar(50) comment '名称',
    primary key(`id`)
)engine=innodb default charset=utf8mb4 comment='大章';




--测试
drop table if exists `NewTable`;
create table `NewTable` (
  `id` char(8) not null default '' comment 'id',
  `name` varchar(50) comment '名称',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='测试';

insert into `NewTable` (id, name) values (1, '测试');
insert into `NewTable` (id, name) values (2, '测试2');