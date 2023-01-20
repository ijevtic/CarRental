INSERT into type (id, name, subject, description) VALUES
                         (1, 'ACTIVATE', 'Account verification',  'Hello %, please activate your account by clicking on the following link: %'),
                         (2, 'PASSWORD', 'Password change', 'Hello %, you have changed your password'),
                         (3, 'RESERVATION', 'New reservation', 'User % has successfully reserved % model in city % from company %! \n Pickup date:%, Dropoff date:%'),
                         (4, 'CANCEL', 'Reservation cancelation', 'Reservation for model % in city % from company % is cancelled! \n Pickup date was:%, Dropoff date was:%'),
                         (5, 'REMINDER', 'Reservation reminder', 'User % has a reservation for model % in city % from company % in 24 hours.');