CREATE TABLE tb_email (
    id_email UUID DEFAULT random_uuid() PRIMARY KEY,
    email_from VARCHAR NOT NULL,
    email_sent_date TIMESTAMP NOT NULL,
    email_status TINYINT NOT NULL,
    email_to VARCHAR NOT NULL,
    owner_ref VARCHAR NOT NULL,
    subject VARCHAR NOT NULL,
    text VARCHAR NOT NULL
);
CREATE TABLE tb_user ( 
    id_user UUID DEFAULT random_uuid() PRIMARY KEY,
    name VARCHAR NOT NULL,
	username VARCHAR NOT NULL UNIQUE,
	password VARCHAR NOT NULL,
	authorities VARCHAR NOT NULL
);