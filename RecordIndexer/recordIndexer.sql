DROP TABLE IF EXISTS "batches";
CREATE TABLE batches
(
	id integer not null primary key autoincrement,
	project_id integer not null,
	dataPath text not null,
	available boolean not null,
	foreign key (project_id) references projects(id)
);
DROP TABLE IF EXISTS "cells";
CREATE TABLE cells
(
	id integer not null primary key autoincrement,
	record_id integer not null,
	field_id integer not null,
	value text,
	foreign key (record_id) references records(id),
	foreign key (field_id) references fields(id)
);
DROP TABLE IF EXISTS "fields";
CREATE TABLE fields
(
	id integer not null primary key autoincrement,
	title text not null,
	project_id integer not null,
	width int not null,
	columnNum int not null,
	firstXCoordinate int not null,
	helpHtml text not null,
	knownData text,
	foreign key (project_id) references projects(id)
);
DROP TABLE IF EXISTS "projects";
CREATE TABLE projects
(
	id integer not null primary key autoincrement,
	title text not null,
	recordsForImage int not null,
	firstYCoordinate int not null,
	recordsHeight int not null
);
DROP TABLE IF EXISTS "records";
CREATE TABLE records
(
	id integer not null primary key autoincrement,
	batch_id integer not null,
	rowNum int not null,
	foreign key (batch_id) references batches(id)
);
DROP TABLE IF EXISTS "users";
CREATE TABLE users
(
	id integer not null primary key autoincrement,
	userName text not null,
	password text not null,
	firstName text not null,
	lastName text not null,
	email text,
	indexedRecords integer not null,
	currentBatchID integer not null
);
