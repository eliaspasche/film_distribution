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
VALUES ('Schaber', 'Nike', '16.04.1909', 'Sonnenhang 171', '27619', N'Schiffdorf');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Flowers', 'Konstanze', '22.09.1918', 'Grazer Straße 110', '56368', N'Ergeshausen');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Siegrist', 'Wolfdietrich', '28.02.2011', 'Bürgermeister-Schmidt-Straße 171', '53534', N'Pomster');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Morales', 'Herold', '17.12.1946', 'Mehrener Straße 39', '06842', N'Dessau');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Hilbig', 'Lisbeth', '02.05.1936', 'Würdinghauser Straße 56', '56459', N'Weltersburg');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Göller', 'Waltrudis', '11.06.2007', 'Pierer Straße 181', '56729', N'Acht');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Rensing', 'Serafine', '06.02.1962', 'Schumannweg 135', '56412', N'Daubach');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Kneer', 'Dietmute', '26.02.1923', 'Gasgartenstraße 165', '16244', N'Finowfurt');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Dannenberg', 'Friederike', '31.12.1943', 'Hellerstraße 130b', '92275', N'Hirschbach');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Abt', 'Adolfa', '09.09.2016', 'Pommerbachstraße 109', '82285', N'Hattenhofen');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Buhlmann', 'Wilfriede', '06.07.1991', 'Niehoffs Blaike 169', '67346', N'Speyer');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Krumme', 'Hartlieb', '09.11.1955', 'Hutteneichenweg 118', '29559', N'Wrestedt');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Kracke', 'Seibold', '30.11.2015', 'In Dangeln 83', '19230', N'Warlitz');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Welke', 'Dörte', '18.03.1983', 'Gerhardstraße 53', '32816', N'Wöbbel');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Vorbeck', 'Rasso', '27.04.1959', 'Heisterbacher Straße 65', '63322', N'Rödermark');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Schlechter', 'Anja', '04.04.2008', 'Ehrenberg 66', '75323', N'Bad Wildbad');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Beek', 'Karlfriedrich', '11.01.1985', 'Geilenkirchener Straße 164', '52066', N'Aachen');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Lutze', 'Marcel', '02.01.1959', 'Lärchenstraße 124a', '17309', N'Pasewalk');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Weiler', 'Annabelle', '04.09.1930', 'Westfalenring 133', '41468', N'Uedesheim');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Thiesen', 'Margunde', '01.02.1910', 'Otto-Hahn-Straße 47', '56377', N'Schweighausen');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Friedrichs', 'Bernd', '24.08.1939', 'Tente 6', '86674', N'Baar');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Senge', 'Sigisbert', '25.01.1933', 'August-Bebel-Straße 139', '84437', N'Reichertsheim');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Rank', 'Bardo', '21.12.1918', 'Jakobusstraße 50', '37293', N'Herleshausen');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Engelbrecht', 'Joschua', '21.06.1911', 'Kindelsbergstraße 180', '54426', N'Heidenburg');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Baumgärtner', 'Heimar', '07.02.1993', 'Auf dem Ebenhahn 100', '56370', N'Rettert');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Weyrich', 'Hildemarie', '12.07.1925', 'Hähnen 5', '33699', N'Bielefeld');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Eisenhardt', 'Finn', '11.05.1927', 'Buchholzstraße 59', '91799', N'Langenaltheim');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Grüninger', 'Trauthilde', '03.10.1947', 'Wahlscheider Straße 133', '76872', N'Freckenfeld');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Preiß', 'Kristina', '18.05.1914', 'Am Bilderstöckchen 158', '76835', N'Roschbach');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Goldstein', 'Sonngard', '06.02.1917', 'Hermannshecke 64', '79241', N'Ihringen');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Köpf', 'Kirstin', '27.07.1948', 'Honsberger Straße 172', '66879', N'Kollweiler');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Lüddecke', 'Collin', '04.02.1972', 'Im Unteren Marktfeld 42', '66894', N'Martinshöhe');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Löwenstein', 'Gunhild', '08.11.1973', 'Lindenhoher Weg 193', '24819', N'Todenbüttel');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Krafft', 'Aloisia', '20.06.1944', 'Biskirchener Straße 133', '85113', N'Böhmfeld');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Gottschling', 'Mirjam', '31.08.1940', 'Am Nelkenberg 1', '78052', N'Villingen-Schwenningen');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Wenisch', 'Heimhart', '01.08.2002', 'Westerfeld 28', '42899', N'Remscheid');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Welke', 'Klementine', '12.12.1976', 'Schlesingstraße 71', '53520', N'Senscheid');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Pritzl', 'Annelies', '12.07.1958', 'Im Brochenbach 91', '86567', N'Hilgertshausen-Tandern');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Hagge', 'Senta', '11.06.2003', 'An der Kirchenwiese 171', '54616', N'Winterspelt');
INSERT INTO customer (name, first_name, date_of_birth, address, zip_code, city)
VALUES ('Stingl', 'Randolf', '17.08.2012', 'Dürener Straße 62', '38685', N'Langelsheim');


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
VALUES (N'Witness for the Prosecution', 6960, (SELECT id FROM age_group WHERE minimum_age = 16), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Whiplash', 6360, (SELECT id FROM age_group WHERE minimum_age = 18), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'WALL·E', 5880, (SELECT id FROM age_group WHERE minimum_age = 0), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Vertigo', 7680, (SELECT id FROM age_group WHERE minimum_age = 6), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Usual Suspects', 6360, (SELECT id FROM age_group WHERE minimum_age = 6), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Sting', 7740, (SELECT id FROM age_group WHERE minimum_age = 0), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Silence of the Lambs', 7080, (SELECT id FROM age_group WHERE minimum_age = 0), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Shining', 8760, (SELECT id FROM age_group WHERE minimum_age = 12), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Shawshank Redemption', 8520, (SELECT id FROM age_group WHERE minimum_age = 0), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Prestige', 7800, (SELECT id FROM age_group WHERE minimum_age = 6), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Pianist', 9000, (SELECT id FROM age_group WHERE minimum_age = 6), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Matrix', 8160, (SELECT id FROM age_group WHERE minimum_age = 12), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Lord of the Rings: The Two Towers', 10740, (SELECT id FROM age_group WHERE minimum_age = 16), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Lord of the Rings: The Return of the King', 12060, (SELECT id FROM age_group WHERE minimum_age = 0),
        0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Lord of the Rings: The Fellowship of the Ring', 10680, (SELECT id FROM age_group WHERE minimum_age = 12),
        0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Lion King', 5280, (SELECT id FROM age_group WHERE minimum_age = 12), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Kid', 4080, (SELECT id FROM age_group WHERE minimum_age = 6), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Green Mile', 11340, (SELECT id FROM age_group WHERE minimum_age = 18), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Great Dictator', 7500, (SELECT id FROM age_group WHERE minimum_age = 12), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Godfather: Part II', 12120, (SELECT id FROM age_group WHERE minimum_age = 6), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Godfather', 10500, (SELECT id FROM age_group WHERE minimum_age = 18), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Departed', 9060, (SELECT id FROM age_group WHERE minimum_age = 18), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Dark Knight Rises', 9840, (SELECT id FROM age_group WHERE minimum_age = 6), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Dark Knight', 9120, (SELECT id FROM age_group WHERE minimum_age = 6), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'The Apartment', 7500, (SELECT id FROM age_group WHERE minimum_age = 12), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Terminator 2: Judgment Day', 8220, (SELECT id FROM age_group WHERE minimum_age = 0), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Taxi Driver', 6840, (SELECT id FROM age_group WHERE minimum_age = 16), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Taare Zameen Par', 9900, (SELECT id FROM age_group WHERE minimum_age = 18), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Sunset Blvd.', 6600, (SELECT id FROM age_group WHERE minimum_age = 6), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Star Wars: Episode VI - Return of the Jedi', 7860, (SELECT id FROM age_group WHERE minimum_age = 16), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Star Wars: Episode V - The Empire Strikes Back', 7440, (SELECT id FROM age_group WHERE minimum_age = 18),
        0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Star Wars', 7260, (SELECT id FROM age_group WHERE minimum_age = 18), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Spider-Man: Into the Spider-Verse', 7020, (SELECT id FROM age_group WHERE minimum_age = 18), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Singin in the Rain', 6180, (SELECT id FROM age_group WHERE minimum_age = 16), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Shichinin no samurai', 12420, (SELECT id FROM age_group WHERE minimum_age = 16), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Sen to Chihiro no kamikakushi', 7500, (SELECT id FROM age_group WHERE minimum_age = 18), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Se7en', 7620, (SELECT id FROM age_group WHERE minimum_age = 0), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Schindlers List', 11700, (SELECT id FROM age_group WHERE minimum_age = 16), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Scarface', 10200, (SELECT id FROM age_group WHERE minimum_age = 18), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Saving Private Ryan', 10140, (SELECT id FROM age_group WHERE minimum_age = 12), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Rear Window', 6720, (SELECT id FROM age_group WHERE minimum_age = 0), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Raiders of the Lost Ark', 6900, (SELECT id FROM age_group WHERE minimum_age = 12), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Pulp Fiction', 9240, (SELECT id FROM age_group WHERE minimum_age = 18), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Psycho', 6540, (SELECT id FROM age_group WHERE minimum_age = 16), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Per qualche dollaro in più', 7920, (SELECT id FROM age_group WHERE minimum_age = 16), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Paths of Glory', 5280, (SELECT id FROM age_group WHERE minimum_age = 12), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'One Flew Over the Cuckoos Nest', 7980, (SELECT id FROM age_group WHERE minimum_age = 0), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Once Upon a Time in America', 13740, (SELECT id FROM age_group WHERE minimum_age = 16), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Oldeuboi', 7200, (SELECT id FROM age_group WHERE minimum_age = 6), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Nuovo Cinema Paradiso', 9300, (SELECT id FROM age_group WHERE minimum_age = 12), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'North by Northwest', 8160, (SELECT id FROM age_group WHERE minimum_age = 6), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Mononoke-hime', 8040, (SELECT id FROM age_group WHERE minimum_age = 16), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Modern Times', 5220, (SELECT id FROM age_group WHERE minimum_age = 0), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Metropolis', 9180, (SELECT id FROM age_group WHERE minimum_age = 6), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Memento', 6780, (SELECT id FROM age_group WHERE minimum_age = 0), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'M - Eine Stadt sucht einen Mörder', 7020, (SELECT id FROM age_group WHERE minimum_age = 0), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Léon', 6600, (SELECT id FROM age_group WHERE minimum_age = 0), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Lawrence of Arabia', 13680, (SELECT id FROM age_group WHERE minimum_age = 16), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Ladri di biciclette', 5340, (SELECT id FROM age_group WHERE minimum_age = 0), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'La vita è bella', 6960, (SELECT id FROM age_group WHERE minimum_age = 6), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Kimi no na wa.', 6360, (SELECT id FROM age_group WHERE minimum_age = 0), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Joker', 7320, (SELECT id FROM age_group WHERE minimum_age = 0), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Its a Wonderful Life', 7800, (SELECT id FROM age_group WHERE minimum_age = 18), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Intouchables', 6720, (SELECT id FROM age_group WHERE minimum_age = 6), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Interstellar', 10140, (SELECT id FROM age_group WHERE minimum_age = 18), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Inception', 8880, (SELECT id FROM age_group WHERE minimum_age = 12), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Il buono, il brutto, il cattivo', 9660, (SELECT id FROM age_group WHERE minimum_age = 18), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Ikiru', 8580, (SELECT id FROM age_group WHERE minimum_age = 0), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Hotaru no haka', 5340, (SELECT id FROM age_group WHERE minimum_age = 12), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Goodfellas', 8760, (SELECT id FROM age_group WHERE minimum_age = 0), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Gladiator', 9300, (SELECT id FROM age_group WHERE minimum_age = 0), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Gisaengchung', 7920, (SELECT id FROM age_group WHERE minimum_age = 12), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Forrest Gump', 8520, (SELECT id FROM age_group WHERE minimum_age = 16), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Fight Club', 8340, (SELECT id FROM age_group WHERE minimum_age = 16), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Dr. Strangelove or: How I Learned to Stop Worrying and Love the Bomb', 5700,
        (SELECT id FROM age_group WHERE minimum_age = 16), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Double Indemnity', 6420, (SELECT id FROM age_group WHERE minimum_age = 18), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Django Unchained', 9900, (SELECT id FROM age_group WHERE minimum_age = 12), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Dil Bechara', 6060, (SELECT id FROM age_group WHERE minimum_age = 12), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Das Leben der Anderen', 8220, (SELECT id FROM age_group WHERE minimum_age = 12), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Das Boot', 8940, (SELECT id FROM age_group WHERE minimum_age = 6), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Dangal', 9660, (SELECT id FROM age_group WHERE minimum_age = 16), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Dag II', 8100, (SELECT id FROM age_group WHERE minimum_age = 12), 7.49);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Coco', 6300, (SELECT id FROM age_group WHERE minimum_age = 6), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'City Lights', 5220, (SELECT id FROM age_group WHERE minimum_age = 18), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Citizen Kane', 7140, (SELECT id FROM age_group WHERE minimum_age = 16), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Cidade de Deus', 7800, (SELECT id FROM age_group WHERE minimum_age = 6), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Casablanca', 6120, (SELECT id FROM age_group WHERE minimum_age = 16), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Capharnaüm', 7560, (SELECT id FROM age_group WHERE minimum_age = 18), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Cera una volta il West', 9900, (SELECT id FROM age_group WHERE minimum_age = 16), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Back to the Future', 6960, (SELECT id FROM age_group WHERE minimum_age = 6), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Avengers: Infinity War', 8940, (SELECT id FROM age_group WHERE minimum_age = 0), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Avengers: Endgame', 10860, (SELECT id FROM age_group WHERE minimum_age = 16), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Apocalypse Now', 8820, (SELECT id FROM age_group WHERE minimum_age = 6), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'American History X', 7140, (SELECT id FROM age_group WHERE minimum_age = 0), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Amadeus', 9600, (SELECT id FROM age_group WHERE minimum_age = 16), 1.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'Alien', 7020, (SELECT id FROM age_group WHERE minimum_age = 0), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'A Clockwork Orange', 8160, (SELECT id FROM age_group WHERE minimum_age = 6), 0.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'3 Idiots', 10200, (SELECT id FROM age_group WHERE minimum_age = 16), 9.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'2001: A Space Odyssey', 8940, (SELECT id FROM age_group WHERE minimum_age = 16), 4.99);
INSERT INTO film (name, length, age_group_id, price)
VALUES (N'12 Angry Men', 5760, (SELECT id FROM age_group WHERE minimum_age = 6), 0.99);

INSERT INTO film_copy (inventory_number, film_id)
SELECT SUBSTR(SYS_GUID(), 1, 15), id
FROM FILM;

INSERT INTO film_copy (inventory_number, film_id)
SELECT SUBSTR(SYS_GUID(), 1, 15), id
FROM FILM;


INSERT INTO film_copy (inventory_number, film_id)
VALUES (SYS_GUID(), (SELECT id FROM film WHERE name = 'Lord of the Rings - The Fellowship'));

commit;