INSERT INTO roles (id, name) VALUES (0, 'USER'), (1, 'ADMIN');
INSERT INTO positions (id, name) VALUES (0, 'CASHIER'), (1, 'SENIOR_CASHIER'), (2, 'COMMODITIES_EXPERT');
INSERT INTO check_status (id, name) VALUES (0, 'CLOSED'), (1, 'MODIFIED'), (2, 'REFUNDED');
INSERT INTO report_types (id, name) VALUES (0, 'CLOSED_CHECKS'), (1, 'MODIFIED_CHECKS'),
(2, 'REFUNDED_CHECKS'), (3, 'MIXED_CHECKS');

INSERT INTO products (title, code, price, quantity_type, quantity_on_stock, image)
VALUES ('Tomato', 5463, 0.75, 'GRAM', 10000, ''::bytea), ('Bread', 87539, 2.3, 'PIECE', 10000, ''::bytea),
('Cheese', 978303, 1.25, 'GRAM', 10000, ''::bytea), ('Cinnamon', 87579, 1., 'GRAM', 10000, ''::bytea),
('Coffee', 46572, 2.5, 'PIECE', 10000, ''::bytea), ('Tea', 78687, 4.8, 'PIECE', 10000, ''::bytea),
('Juice', 20402, 1.75, 'PIECE', 10000, ''::bytea), ('Orange', 34205, 0.55, 'GRAM', 10000, ''::bytea),
('Apple', 86744, 0.15, 'GRAM', 10000, ''::bytea), ('Pork', 712634, 2., 'GRAM', 10000, ''::bytea);

INSERT INTO employees (first_name, last_name, email, salary, position_id)
VALUES ('John', 'Williams', 'john@gmail.com', 300.0, 0), ('Kate', 'Miller', 'kate@gmail.com', 350.0, 0),
('Jack', 'Smith', 'jack@gmail.com', 500.0, 1);

INSERT INTO checks (employee_id, sum, check_date, status_id) VALUES (1, 494.4, '1-15-2018 10:45', 0),
(2, 190.25, '1-10-2018 13:30', 0), (3, 844.4, '1-5-2018 21:50', 0);

INSERT INTO checks_products (check_id, product_id, bought_quantity) VALUES (1, 1, 200), (1, 2, 3), (1, 3, 150),
(2, 9, 200), (2, 7, 3), (2, 4, 150), (2, 5, 2), (3, 10, 300), (3, 6, 1), (3, 8, 200), (3, 3, 100), (3, 2, 2);

INSERT INTO  reports (since_date , until_date, total_sum, creation_date, type_id)
VALUES ('1-4-2018 8:00', '1-12-2018 23:00', 845.5, '1-13-2018 12:00', 0);

INSERT INTO reports_checks (report_id, check_id) VALUES (1, 2);
INSERT INTO reports_checks (report_id, check_id) VALUES (1, 3);