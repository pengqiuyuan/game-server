drop table if exists nhdtask;
drop table if exists nhduser;

create table nhd_task (
	id bigint auto_increment,
	title varchar(128) not null,
	description varchar(255),
	user_id bigint not null,
    primary key (id)
) engine=InnoDB;

create table nhd_user (
	id bigint auto_increment,
	login_name varchar(64) not null unique,
	name varchar(64) not null,
	password varchar(255) not null,
	salt varchar(64) not null,
	roles varchar(255) not null,
	register_date timestamp not null default 0,
	primary key (id)
) engine=InnoDB;


CREATE TABLE `hk_seq_reg` (
  `id_seq_reg` int(11) NOT NULL AUTO_INCREMENT,
  `sync_flag` varchar(255) DEFAULT NULL,
  `seq_type` varchar(255) DEFAULT NULL,
  `section` varchar(255) DEFAULT NULL,
  `lo_ctr` bigint(20) DEFAULT NULL,
  `hi_ctr` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_seq_reg`)
) engine=InnoDB DEFAULT CHARSET=utf8;