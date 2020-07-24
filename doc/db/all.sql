

--测试
drop table if exists `NewTable`;
create table `NewTable` (
  `id` char(8) not null default '' comment 'id',
  `name` varchar(50) comment '名称',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='测试';

insert into `NewTable` (id, name) values (1, '测试');
insert into `NewTable` (id, name) values (2, '测试2');