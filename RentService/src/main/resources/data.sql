INSERT INTO company (id, company_name, description) VALUES (1, "Firma Firmic", "firmasto")
INSERT INTO car_type (id, type_name) VALUES (1, 'SUV'), (2, 'Sedan'), (3, 'Hatchback');
INSERT INTO car_model (id, model_name, price, car_type_id, company_id) VALUES (1, "clio", 500, 3, 1);
INSERT INTO location (id, city) VALUES (1, 'Belgrade'), (2, 'Vienna'), (3, 'Novi Sad');

INSERT INTO location (id, city) VALUES (4, 'Zajecar');

INSERT INTO vehicle (id, car_model_id, location_id) VALUES (1,1,2);

INSERT INTO reservation (id,start_time, end_time, vehicle_id, client_id) values (1,10,20,1,8);

INSERT INTO reservation (id,start_time, end_time, vehicle_id, client_id) values (2,30,40,1,8);
insert into review (id, comment, mark, reservation_id) values (1, "nice", 5, 2);
INSERT INTO reservation (id,start_time, end_time, vehicle_id, client_id) values (3,30,40,1,10);


INSERT INTO company (id, company_name, description) values (1, "Firma Firmic", "firmasto"),
                                                           (2, "Ozbiljno ime kompanije", "firmasticno");