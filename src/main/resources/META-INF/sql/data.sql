insert into management.users
    (name,          email,            password) values
    ('John Wick',   'john@wick.com',  '$trongP@$$w0rd'),
    ('John Rambo',  'john@rambo.com', 'Even$trongerP@$$w0rd');
insert into management.airports
    (name,                                      code,   city,       created_by, create_date) values
    ('King Khalid International Airport',       'RUH',  'Riyadh',   1,          '2023/03/29'),
    ('Heathrow Airport',                        'LHR',  'London',   1,          '2023/04/02'),
    ('John F. Kennedy International Airport',   'JFK',  'New York', 2,          '2023/04/05');