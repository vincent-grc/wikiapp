drop table if exists `test`;
create table `test` (
                        `id` bigint not null comment  'id',
                        `name` varchar(50) comment  'name',
                        `password` varchar(50) comment  'password',
                        primary key (`id`)
)engine = innodb default charset=utf8mb4 comment = 'testing';

insert into `test` (id, name, password) values (1, 'test', '123');

drop table if exists `demo`;
create table `demo` (
                        `id` bigint not null comment  'id',
                        `name` varchar(50) comment  'name',
                        primary key (`id`)
)engine = innodb default charset=utf8mb4 comment = 'testing';

insert into `demo` (id, name) values (1, 'test');

drop table if exists `ebook`;
create table `ebook` (
                         `id` bigint not null comment 'id',
                         `name` varchar(50) comment 'book_name',
                         `category1_id` bigint comment 'category_level1',
                         `category2_id` bigint comment 'category_level2',
                         `description` varchar(200) comment 'description',
                         `cover` varchar(200) comment 'cover',
                         `doc_count` int not null default 0 comment 'documents_count',
                         `view_count` int not null default 0 comment 'number_of_readers',
                         `vote_count` int not null default 0 comment 'number_of_likes',
                         primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='ebook';

insert into `ebook` (id, name, description) values (1, 'Spring Boot Tutorial', 'Beginner Friendly Java devlopment，The preferred framework for enterprise application development');
insert into `ebook` (id, name, description) values (2, 'Vue Tutorial', 'Beginner Friendly Vue development，The preferred framework for enterprise application development');
insert into `ebook` (id, name, description) values (3, 'Python Tutorial', 'Beginner Friendly Python development，The preferred framework for enterprise application development');
insert into `ebook` (id, name, description) values (4, 'Mysql Tutorial', 'Beginner Friendly Mysql development，The preferred framework for enterprise application development');
insert into `ebook` (id, name, description) values (5, 'Oracle Tutorial', 'Beginner Friendly Oracle development，The preferred framework for enterprise application development');
