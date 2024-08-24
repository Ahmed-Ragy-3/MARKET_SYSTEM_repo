CREATE TABLE items (
    id          NUMBER (6)  PRIMARY KEY, 
    name        VARCHAR2 (50),
    price       NUMBER (8, 2) 
);

INSERT INTO items (id, name, price) VALUES (1, 'Laptop', 999.99);
INSERT INTO items (id, name, price) VALUES (2, 'Smartphone', 699.99);
INSERT INTO items (id, name, price) VALUES (3, 'Tablet', 399.50);
INSERT INTO items (id, name, price) VALUES (4, 'Smartwatch', 199.99);
INSERT INTO items (id, name, price) VALUES (5, 'Headphones', 89.99);
INSERT INTO items (id, name, price) VALUES (6, 'Monitor', 249.99);
INSERT INTO items (id, name, price) VALUES (7, 'Keyboard', 49.99);
INSERT INTO items (id, name, price) VALUES (8, 'Mouse', 29.99);
INSERT INTO items (id, name, price) VALUES (9, 'Printer', 150.00);
INSERT INTO items (id, name, price) VALUES (10, 'External Hard Drive', 75.00);

select * from items;