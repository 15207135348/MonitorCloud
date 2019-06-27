CREATE DATABASE IF NOT EXISTS `monitorCloud` DEFAULT CHARACTER SET utf8;

USE `monitorCloud`;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `code` varchar(255) DEFAULT NULL COMMENT '邮箱激活码',
  `enabled` SMALLINT(0) DEFAULT NULL COMMENT '是否激活',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
insert into `user`(`id`,`username`,`password`,`email`,`code`,`enabled`) values (1,'admin','root','1365733349@qq.com',null,1);

-- 被监控的主机
DROP TABLE IF EXISTS `monitored_host`;
CREATE TABLE `monitored_host` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `hostname` varchar(255) DEFAULT NULL COMMENT '主机名',
  `system` varchar(255) DEFAULT NULL COMMENT '操作系统',
  `mac` varchar(255) DEFAULT NULL COMMENT 'MAC地址',
  `ipv4` varchar(255) DEFAULT NULL COMMENT 'IPv4地址',
  `cpu` varchar(255) DEFAULT NULL COMMENT 'cpu数量',
  `ram` varchar(255) DEFAULT NULL COMMENT '内存大小GB',
  `disk` varchar(255) DEFAULT NULL COMMENT '磁盘大小GB',
  `connected` SMALLINT(0) DEFAULT NULL COMMENT '主机是否连接',
  `timestamp` bigint(20) DEFAULT NULL COMMENT '接入时间',
  `username` varchar(255) DEFAULT NULL COMMENT '所属用户',
  PRIMARY KEY (`id`),
  UNIQUE KEY `mac` (`mac`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- IP列表
DROP TABLE IF EXISTS `iptables`;
CREATE TABLE `iptables` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ip` varchar(255) DEFAULT NULL COMMENT 'ip地址',
  `enalbe` SMALLINT(0) DEFAULT NULL COMMENT '是否使能',
  `reason` varchar(255) DEFAULT NULL COMMENT '原因',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ip` (`ip`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 异常记录
DROP TABLE IF EXISTS `anomaly_record`;
CREATE TABLE `anomaly_record` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `mac` varchar(255) DEFAULT NULL COMMENT '异常主机',
  `timestamp` bigint(20) DEFAULT NULL COMMENT '异常时间',
  `description` varchar(255) DEFAULT NULL COMMENT '异常描述',
  `logs` varchar(255) DEFAULT NULL COMMENT '日志路径',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `hbase_qualifier`;
CREATE TABLE `hbase_qualifier` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) DEFAULT NULL COMMENT '名字',
  `description` varchar(255) DEFAULT NULL COMMENT '注解',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;
insert into `hbase_qualifier`(`id`,`name`,`description`) values (1,'a','User(%)');
insert into `hbase_qualifier`(`id`,`name`,`description`) values (2,'b','Nice(%)');
insert into `hbase_qualifier`(`id`,`name`,`description`) values (3,'c','System(%)');
insert into `hbase_qualifier`(`id`,`name`,`description`) values (4,'d','Idle(%)');
insert into `hbase_qualifier`(`id`,`name`,`description`) values (5,'e','Wait(%)');
insert into `hbase_qualifier`(`id`,`name`,`description`) values (6,'f','Irq(%)');
insert into `hbase_qualifier`(`id`,`name`,`description`) values (7,'g','SoftIrq(%)');

insert into `hbase_qualifier`(`id`,`name`,`description`) values (8,'h','Cached(%)');
insert into `hbase_qualifier`(`id`,`name`,`description`) values (9,'i','Buffers(%)');
insert into `hbase_qualifier`(`id`,`name`,`description`) values (10,'j','Actual(%)');

insert into `hbase_qualifier`(`id`,`name`,`description`) values (11,'k','磁盘读速率(kb/s)');
insert into `hbase_qualifier`(`id`,`name`,`description`) values (12,'l','磁盘写速率(kb/s)');
insert into `hbase_qualifier`(`id`,`name`,`description`) values (13,'m','磁盘读次数');
insert into `hbase_qualifier`(`id`,`name`,`description`) values (14,'n','磁盘写次数');

insert into `hbase_qualifier`(`id`,`name`,`description`) values (15,'o','流量出栈速率(kb/s)');
insert into `hbase_qualifier`(`id`,`name`,`description`) values (16,'p','流量入栈速率(kb/s)');