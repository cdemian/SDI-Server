drop database if exists sdi;
create database sdi;
use sdi;

create table students(
	id int not null AUTO_INCREMENT,
	name varchar(100) not null,
	gr varchar(100) not null,
	PRIMARY KEY (id)
	);
	
insert into Students values(null, 'Adam', '932');
