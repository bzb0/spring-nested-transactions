DROP TABLE IF EXISTS project;

CREATE TABLE project (
	id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name varchar(30),
	description varchar(64)
);