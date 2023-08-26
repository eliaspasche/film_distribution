
insert into sample_person(version, id,first_name,last_name,email,phone,date_of_birth,occupation,role,important) values (1, 1,'Eula','Lane','eula.lane@jigrormo.ye','(762) 526-5961','1954-01-18','Insurance Clerk','Worker',false)
insert into sample_person(version, id,first_name,last_name,email,phone,date_of_birth,occupation,role,important) values (1, 2,'Barry','Rodriquez','barry.rodriquez@zun.mm','(267) 955-5124','2013-01-18','Mortarman','Manager',false)
insert into sample_person(version, id,first_name,last_name,email,phone,date_of_birth,occupation,role,important) values (1, 3,'Eugenia','Selvi','eugenia.selvi@capfad.vn','(680) 368-2192','1973-01-03','Beer Coil Cleaner','External',false)

insert into application_user (version, id, username,name,hashed_password) values (1, '1','user','John Normal','$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe')
insert into user_roles (user_id, roles) values ('1', 'USER')
insert into application_user (version, id, username,name,hashed_password) values (1, '2','admin','Emma Powerful','$2a$10$jpLNVNeA7Ar/ZQ2DKbKCm.MuT2ESe.Qop96jipKMq7RaUgCoQedV.')
insert into user_roles (user_id, roles) values ('2', 'USER')
insert into user_roles (user_id, roles) values ('2', 'ADMIN')
