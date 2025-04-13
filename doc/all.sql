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

ALTER TABLE ebook MODIFY id BIGINT AUTO_INCREMENT;

drop table if exists `category`;

# Classification
drop table if exists `category`;
create table `category` (
                            `id` bigint not null comment 'id',
                            `parent` bigint not null default 0 comment 'parent_id',
                            `name` varchar(50) not null comment 'name',
                            `sort` int comment 'order',
                            primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='classification';

insert into `category` (id, parent, name, sort) values (100, 000, 'Frontend Development', 100);
insert into `category` (id, parent, name, sort) values (101, 100, 'Vue', 101);
insert into `category` (id, parent, name, sort) values (102, 100, 'HTML & CSS', 102);
insert into `category` (id, parent, name, sort) values (200, 000, 'Java', 200);
insert into `category` (id, parent, name, sort) values (201, 200, 'Basic Applications', 201);
insert into `category` (id, parent, name, sort) values (202, 200, 'Framework Applications', 202);
insert into `category` (id, parent, name, sort) values (300, 000, 'Python', 300);
insert into `category` (id, parent, name, sort) values (301, 300, 'Basic Applications', 301);
insert into `category` (id, parent, name, sort) values (302, 300, 'Advanced Applications', 302);
insert into `category` (id, parent, name, sort) values (400, 000, 'Database', 400);
insert into `category` (id, parent, name, sort) values (401, 400, 'MySQL', 401);
insert into `category` (id, parent, name, sort) values (500, 000, 'Others', 500);
insert into `category` (id, parent, name, sort) values (501, 500, 'Server', 501);
insert into `category` (id, parent, name, sort) values (502, 500, 'Development Tools', 502);
insert into `category` (id, parent, name, sort) values (503, 500, 'Popular Backend Languages', 503);

ALTER TABLE category
MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;