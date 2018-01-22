INSERT INTO roles (id, name) VALUES (0, 'USER'), (1, 'ADMIN');
INSERT INTO positions (id, name) VALUES (0, 'CASHIER'), (1, 'SENIOR_CASHIER'), (2, 'COMMODITIES_EXPERT'),
(3, 'ADMINISTRATOR');
INSERT INTO check_status (id, name) VALUES (0, 'CLOSED'), (1, 'MODIFIED'), (2, 'REFUNDED');
INSERT INTO report_types (id, name) VALUES (0, 'CLOSED_CHECKS'), (1, 'MODIFIED_CHECKS'),
(2, 'REFUNDED_CHECKS'), (3, 'MIXED_CHECKS');

INSERT INTO products (title, code, price, quantity_type, quantity_on_stock, image)
VALUES ('Tomato', 5463, 0.75, 'GRAM', 10000, ''::bytea), ('Bread', 87539, 2.3, 'PIECE', 10000, ''::bytea),
('Cheese', 978303, 1.25, 'GRAM', 10000, ''::bytea), ('Cinnamon', 87579, 1., 'GRAM', 10000, ''::bytea),
('Coffee', 46572, 2.5, 'PIECE', 10000, ''::bytea), ('Tea', 78687, 4.8, 'PIECE', 10000, ''::bytea),
('Juice', 20402, 1.75, 'PIECE', 10000, ''::bytea), ('Orange', 34205, 0.55, 'GRAM', 10000, ''::bytea),
('Apple', 86744, 0.15, 'GRAM', 10000, ''::bytea), ('Pork', 712634, 2., 'GRAM', 10000, ''::bytea),
('Beef', 6854, 2.45, 'GRAM', 10000, ''::bytea), ('Salmon', 4598, 2.8, 'GRAM', 10000, ''::bytea),
('Cake', 535275, 0.35, 'PIECE', 10000, ''::bytea),('Cookie', 976986, 0.1, 'GRAM', 10000, ''::bytea),
('Cucumber', 1278243, 0.2, 'GRAM', 10000, ''::bytea), ('Carrot', 29478, 0.25, 'GRAM', 10000, ''::bytea),
('Chocolate', 6745830, 0.75, 'GRAM', 10000, ''::bytea), ('Chicken', 36554, 1.8, 'GRAM', 10000, ''::bytea),
('Candy', 23568, 0.1, 'GRAM', 10000, ''::bytea);

INSERT INTO employees (first_name, last_name, email, salary, position_id)
VALUES ('John', 'Williams', 'john@gmail.com', 300.0, 0), ('Kate', 'Miller', 'kate@gmail.com', 350.0, 0),
('Jack', 'Smith', 'jack@gmail.com', 320.0, 1), ('Rick', 'Wilson', 'rick@gmail.com', 425.0, 2),
('Richard', 'Harris', 'richard@gmail.com', 480.0, 3);

INSERT INTO cash_register_users (login, password, role_id, employee_id)
VALUES ('johnLogin', '337ff044511071b56bc5ea5e4320fc35d33a391b4d27afb7d91aef77ac71bdb6', 0, 1),
('kateLogin', 'e47f04d6765f92ce1e5268f59d10e2a92eceb085d1a76a965a2efdb44d4911c4', 0, 2),
('jackLogin', '797eb4ad6e0394d84277a076381c603fd3da0cc0bca3e6d8e1f86e6126f32757', 0, 3),
('rickLogin', '9ae32504496f595f63e41a0578f5759ba3417bbfbf88925145795862b0c68c37', 0, 4),
('richardLogin', '7064cf13a720ce6a7c01d2d19ae5db32ae29fa7fc78c81afab56e7690fbb5667', 0, 5);

INSERT INTO checks (employee_id, sum, check_date, status_id) VALUES (1, 344.4, '1-15-2018 10:45', 0),
(2, 190.25, '1-10-2018 13:30', 0), (3, 844.4, '1-5-2018 21:50', 0), (3, 739.6, '1-3-2018 20:50', 0),
(2, 509.55, '12-25-2017 14:36', 0), (1, 159.6, '12-20-2017 9:00', 1), (1, 610, '1-11-2018 22:45', 1),
(3, 1112.3, '12-27-2017 20:05', 1), (2, 758.5, '1-17-2018 12:50', 2), (1, 977.5, '12-30-2017 21:22', 2);

INSERT INTO checks_products (check_id, product_id, bought_quantity) VALUES (1, 1, 200), (1, 2, 3), (1, 3, 150),
(2, 9, 200), (2, 7, 3), (2, 4, 150), (2, 5, 2), (3, 10, 300), (3, 6, 1), (3, 8, 200), (3, 3, 100), (3, 2, 2),
(4, 15, 300), (4, 11, 200), (4, 8, 300), (4, 14, 150), (4, 6, 2),
(5, 19, 300), (5, 18, 200), (5, 1, 2), (5, 7, 1), (5, 13, 2), (5, 17, 150),
(6, 4, 100), (6, 6, 2), (6, 16, 200), (7, 9, 250), (7, 5, 1), (7, 12, 200), (7, 19, 100),
(8, 14, 300), (8, 8, 300), (8, 15, 200), (8, 16, 150), (8, 18, 200), (8, 17, 300), (8, 2, 1), (8, 3, 200), (8, 5, 1),
(9, 11, 300), (9, 19, 200), (9, 7, 2), (10, 12, 300), (10, 8, 250);

INSERT INTO  reports (since_date , until_date, total_sum, creation_date, type_id)
VALUES ('1-4-2018 8:00', '1-12-2018 23:00', 1774.25, '1-13-2018 12:00', 0),
('12-20-2017 9:00', '1-18-2018 22:00', 2628.2, '1-18-2018 23:00', 0),
('12-15-2017 8:30', '1-15-2018 23:50', 1881.9, '1-17-2018 13:00', 1),
('12-18-2017 8:00', '1-18-2018 23:00', 1736., '1-19-2018 11:00', 2),
('12-1-2017 8:00', '1-18-2018 23:00', 1903.15, '1-20-2018 15:00', 3);

INSERT INTO reports_checks (report_id, check_id) VALUES (1, 2), (1, 3), (1, 4);
INSERT INTO reports_checks (report_id, check_id) VALUES (2, 1), (2, 2), (2, 3), (2, 4), (2, 5);
INSERT INTO reports_checks (report_id, check_id) VALUES (3, 6), (3, 7), (3, 8);
INSERT INTO reports_checks (report_id, check_id) VALUES (4, 9), (3, 10);
INSERT INTO reports_checks (report_id, check_id) VALUES (5, 1), (5, 2), (5, 7), (5, 9);