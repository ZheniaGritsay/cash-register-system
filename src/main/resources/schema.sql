CREATE TABLE positions (
	id int PRIMARY KEY,
	name varchar(255) NOT NULL
);

CREATE TABLE roles (
	id int PRIMARY KEY,
	name varchar(255) NOT NULL
);

CREATE TABLE check_status (
	id int PRIMARY KEY,
	name varchar(255) NOT NULL
);

CREATE TABLE report_types (
	id int PRIMARY KEY,
	name varchar(255) NOT NULL
);

CREATE TABLE products (
	id serial PRIMARY KEY,
	title varchar(255) NOT NULL,
	code integer NOT NULL,
	price decimal(5, 2) NOT NULL,
	quantity_type varchar(255) NOT NULL,
	quantity_on_stock integer NOT NULL,
	image bytea
);

CREATE TABLE employees (
	id serial PRIMARY KEY,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	salary decimal(5, 2) NOT NULL,
	position_id integer REFERENCES positions (id)
);

CREATE TABLE checks (
	id serial PRIMARY KEY,
	employee_id bigint REFERENCES employees (id),
	sum decimal(5, 2) NOT NULL,
	check_date timestamp without time zone NOT NULL,
	status_id integer REFERENCES check_status (id)
);

CREATE TABLE checks_products (
	check_id bigint REFERENCES checks (id) ON DELETE CASCADE,
	product_id bigint REFERENCES products (id) ON DELETE CASCADE,
	bought_quantity integer NOT NULL,
	PRIMARY KEY (check_id, product_id)
);

CREATE TABLE reports (
	id serial PRIMARY KEY,
	since_date timestamp without time zone NOT NULL,
	until_date timestamp without time zone NOT NULL,
	total_sum decimal(5, 2) NOT NULL,
	creation_date timestamp without time zone NOT NULL,
	type_id integer REFERENCES report_types (id)
);

CREATE TABLE reports_checks (
	report_id bigint REFERENCES reports (id),
	check_id bigint REFERENCES checks (id),
	PRIMARY KEY (report_id, check_id)
);