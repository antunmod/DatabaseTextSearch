create table movie(
	id SERIAL UNIQUE NOT NULL,
	title varchar(50),
	summary varchar(100),
	description varchar(500));

create table category(
	id SERIAL UNIQUE NOT NULL,
	name varchar(20) UNIQUE);

create table movie_category(
	movie_id bigint REFERENCES movie(id),
	category_id bigint REFERENCES category(id));