-- noinspection SqlWithoutWhereForFile
DELETE
FROM application_user;
DELETE
FROM customer;
DELETE
FROM film;
DELETE
FROM age_group;
DELETE
FROM film_copy;

-- login: admin | pw: admin
INSERT INTO application_user (username, name, hashed_password, user_role)
VALUES ('admin', 'Admin Account', '$2a$10$imjfqJuXRgDUqIMhskmZoucc4dO1TSKNqcmEy1uoakpvJD2lPvvzK', 'ADMIN');
-- login: jah | pw: user
INSERT INTO application_user (username, name, hashed_password)
VALUES ('jah', 'Jan Heinrich', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe');
-- login: epa | pw: user
INSERT INTO application_user (username, name, hashed_password)
VALUES ('epa', 'Elias Pasche', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe');
-- login: user | pw: user
INSERT INTO application_user (username, name, hashed_password)
VALUES ('user', 'Default User', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe');


INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Customer', 'First', '01.01.2000', 'Streets 2b', '37073', N'Göttingen');


INSERT INTO age_group (name, minimum_age)
VALUES ('FSK 0', 0);
INSERT INTO age_group (name, minimum_age)
VALUES ('FSK 6', 6);
INSERT INTO age_group (name, minimum_age)
VALUES ('FSK 12', 12);
INSERT INTO age_group (name, minimum_age)
VALUES ('FSK 16', 16);
INSERT INTO age_group (name, minimum_age)
VALUES ('FSK 18', 18);


INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Lord of the Rings - The Fellowship', 30000, (SELECT id FROM age_group WHERE minimum_age = 12), 4.99);


INSERT INTO film_copy (inventory_number, film_id)
VALUES (SYS_GUID(), (SELECT id FROM film WHERE name = 'Lord of the Rings - The Fellowship'));

commit;