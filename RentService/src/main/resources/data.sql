INSERT INTO car_type (id, type_name) VALUES (1, 'SUV'), (2, 'Sedan'), (3, 'Hatchback');
INSERT INTO location (id, city) VALUES (1, 'Belgrade'), (2, 'Vienna'), (3, 'Novi Sad');

INSERT INTO location (id, city) VALUES (4, 'Zajecar');

INSERT INTO vehicle (id, car_model_id, location_id) VALUES (2,1,2);

INSERT INTO reservation (id,start_time, end_time, vehicle_id, client_id) values (1,10,20,1,8);

INSERT INTO reservation (id,start_time, end_time, vehicle_id, client_id) values (2,30,40,1,8);