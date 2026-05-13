CREATE SCHEMA sep2;
SET SCHEMA 'sep2';

create table propertyOwner(
    id serial primary key,
    first_name varchar(50) NOT NULL,
    last_name varchar(50) NOT NULL,
    email varchar(50) NOT NULL,
    phoneNumber varchar(10)NOT NULL,
    CHECK (phoneNumber ~ '^[0-9]{10}$'),
    username varchar(50) NOT NULL,
    password varchar (50) NOT NULL,
    dateOfBirth date NOT NULL,
    gender varchar(20) NOT NULL,
    nationality varchar(40) NOT NULL,
    numberOfListings integer NOT NULL
);

CREATE TABLE admin(
    id serial primary key,
    adminName varchar(50) NOT NULL,
    username varchar(50) NOT NULL,
    password varchar(50) NOT NULL
);

CREATE TABLE client(
    id serial primary key,
    firstName varchar(50) NOT NULL,
    lastName varchar(50) NOT NULL,
    email varchar(50) NOT NULL,
    phoneNumber varchar(10) NOT NULL,
     CHECK (phoneNumber ~ '^[0-9]{10}$'),
    username varchar(50) NOT NULL,
    password varchar(50) NOT NULL,
    dateOfBirth date NOT NULL,
    gender varchar(20) NOT NULL,
    nationality varchar(40) NOT NULL
);

CREATE TABLE ownerApplication(
    applicationId serial primary key,
    clientId integer references client(id) not null,
    adminId integer references admin(id) not null,
    submissionDate date not null,
    status varchar(50) not null,
    propertyAddress varchar(100) not null,
    propertyRegistrationNumber varchar(100) not null
);

CREATE TABLE listing(
    id serial primary key ,
    number_of_rooms INTEGER NOT NULL CHECK (number_of_rooms > 0),
    number_of_bathrooms INTEGER NOT NULL CHECK (number_of_bathrooms > 0),
    has_balcony BOOLEAN NOT NULL,
    surface_area INTEGER NOT NULL CHECK (surface_area > 0),
    price INTEGER NOT NULL CHECK (price >= 0),
    last_Renovated DATE,
    max_number_of_people  INTEGER NOT NULL CHECK (max_number_of_people > 0),
    country VARCHAR(100) NOT NULL,
    region VARCHAR(100) NOT NULL,
    street VARCHAR (100) NOT NULL,
    room_number VARCHAR(20) NOT NULL
);



CREATE TABLE Booking (
    clientId INTEGER NOT NULL,
    listingId INTEGER NOT NULL,
    start_Date DATE,
    end_Date DATE,
    check_in_time TIME NOT NULL,
    check_out_time TIME NOT NULL,
    number_of_people INTEGER NOT NULL CHECK (number_of_people > 0)

);
CREATE TABLE city (
    postal_code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Insert into propertyOwner
INSERT INTO propertyOwner
(first_name, last_name, email, phoneNumber, username, password, dateOfBirth, gender, nationality, numberOfListings)
VALUES
('Carlos', 'Mendez', 'carlos.mendez@email.com', '1234567890', 'cmendez', 'pass123', '1980-05-14', 'Male', 'Spanish', 4),

('Aisha', 'Khan', 'aisha.khan@email.com', '2345678901', 'aishak', 'secure321', '1992-11-22', 'Female', 'Pakistani', 2),

('Lars', 'Jensen', 'lars.jensen@email.com', '3456789012', 'ljensen', 'denmark77', '1975-03-09', 'Male', 'Danish', 6),

('Yuki', 'Tanaka', 'yuki.tanaka@email.com', '4567890123', 'yukit', 'tokyo999', '1988-07-18', 'Female', 'Japanese', 3),

('Amara', 'Okafor', 'amara.okafor@email.com', '5678901234', 'amarao', 'naija22', '1990-01-30', 'Female', 'Nigerian', 5);

-- Insert into admin
INSERT INTO admin (adminName) VALUES
('Michael Scott'),
('Sophia Andersen'),
('Ahmed Hassan');

-- Insert into client
INSERT INTO client
(firstName, lastName, email, phoneNumber, username, password, dateOfBirth, gender, nationality)
VALUES
('Emily', 'Johnson', 'emily.johnson@email.com', '6789012345', 'emilyj', 'hello123', '1998-04-12', 'Female', 'American'),

('Ravi', 'Patel', 'ravi.patel@email.com', '7890123456', 'ravip', 'india456', '1995-09-25', 'Male', 'Indian'),

('Chen', 'Wei', 'chen.wei@email.com', '8901234567', 'chenw', 'china789', '2000-02-15', 'Male', 'Chinese'),

('Fatima', 'Al-Farsi', 'fatima.farsi@email.com', '9012345678', 'fatimaf', 'oman321', '1997-06-20', 'Female', 'Omani'),

('Lucas', 'Silva', 'lucas.silva@email.com', '0123456789', 'lucass', 'brazil11', '1993-12-08', 'Male', 'Brazilian');

-- Insert into ownerApplication
INSERT INTO ownerApplication
(clientId, adminId, submissionDate, status, propertyAddress, propertyRegistrationNumber)
VALUES
(1, 1, '2026-01-10', 'Approved', '12 Green Street, Madrid', 'REG-MAD-001'),

(2, 2, '2026-02-14', 'Pending', '45 Lake View, Mumbai', 'REG-MUM-002'),

(3, 3, '2026-03-05', 'Rejected', '88 Pearl Road, Beijing', 'REG-BEI-003'),

(4, 1, '2026-03-20', 'Approved', '7 Desert Avenue, Muscat', 'REG-MUS-004');

-- Insert into listing
INSERT INTO listing
(number_of_rooms, number_of_bathrooms, has_balcony, surface_area, price,
 last_Renovated, max_number_of_people, country, region, street, room_number)
VALUES
(3, 2, TRUE, 120, 1500, '2023-06-15', 5, 'Spain', 'Catalonia', 'La Rambla Street', 'A12'),

(2, 1, FALSE, 80, 900, '2022-09-10', 3, 'Denmark', 'Midtjylland', 'Sondergade', 'B5'),

(4, 3, TRUE, 200, 2500, '2024-01-22', 8, 'Japan', 'Tokyo', 'Shibuya Road', 'C21'),

(1, 1, FALSE, 45, 600, '2021-11-05', 2, 'Brazil', 'Sao Paulo', 'Palm Street', 'D9'),

(5, 4, TRUE, 300, 4000, '2025-02-18', 10, 'Nigeria', 'Lagos', 'Victoria Island Ave', 'E30');

-- Insert into Booking
INSERT INTO Booking
(clientId, listingId, start_Date, end_Date, check_in_time, check_out_time, number_of_people)
VALUES
(1, 1, '2026-06-01', '2026-06-07', '14:00:00', '11:00:00', 2),

(2, 3, '2026-07-10', '2026-07-20', '15:00:00', '10:00:00', 4),

(3, 2, '2026-08-05', '2026-08-12', '13:30:00', '11:30:00', 1),

(4, 5, '2026-09-01', '2026-09-15', '16:00:00', '12:00:00', 6);

-- Insert into city
INSERT INTO city (postal_code, name) VALUES
('1000', 'Copenhagen'),
('8700', 'Horsens'),
('2800', 'Madrid'),
('110001', 'Delhi'),
('100-0001', 'Tokyo');