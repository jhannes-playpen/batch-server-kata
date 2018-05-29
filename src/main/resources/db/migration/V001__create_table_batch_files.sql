create table batch_files (
	id varchar(50) primary key,
	file_location varchar(100) not null,
	file_length integer,
	submitted_filename varchar(100),
	upload_time timestamp
);
