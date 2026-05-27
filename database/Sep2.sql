-- =============================================================
--  Sep2 -- Full schema + seed data
--  Run this file once on a clean database to set everything up.
-- =============================================================

-- ---------------------------------------------------------------
-- Schema
-- ---------------------------------------------------------------
create database roomrental;
CREATE SCHEMA IF NOT EXISTS sep2;
SET SCHEMA 'sep2';

-- ---------------------------------------------------------------
-- Tables
-- ---------------------------------------------------------------

CREATE TABLE city (
    postal_code VARCHAR(20) PRIMARY KEY,
    name        VARCHAR(100) NOT NULL
);

CREATE TABLE propertyOwner (
    id               SERIAL PRIMARY KEY,
    first_name       VARCHAR(50)  NOT NULL,
    last_name        VARCHAR(50)  NOT NULL,
    email            VARCHAR(50)  NOT NULL,
    phoneNumber      VARCHAR(10)  NOT NULL,
    CHECK (phoneNumber ~ '^[0-9]{10}$'),
    username         VARCHAR(50)  NOT NULL,
    password         VARCHAR(50)  NOT NULL,
    dateOfBirth      DATE         NOT NULL,
    gender           VARCHAR(20)  NOT NULL,
    nationality      VARCHAR(40)  NOT NULL,
    numberOfListings INTEGER      NOT NULL
);

CREATE TABLE admin (
    id        SERIAL PRIMARY KEY,
    adminName VARCHAR(50) NOT NULL,
    username  VARCHAR(50) NOT NULL,
    password  VARCHAR(50) NOT NULL
);

CREATE TABLE client (
    id          SERIAL PRIMARY KEY,
    firstName   VARCHAR(50) NOT NULL,
    lastName    VARCHAR(50) NOT NULL,
    email       VARCHAR(50) NOT NULL,
    phoneNumber VARCHAR(10) NOT NULL,
    CHECK (phoneNumber ~ '^[0-9]{10}$'),
    username    VARCHAR(50) NOT NULL,
    password    VARCHAR(50) NOT NULL,
    dateOfBirth DATE        NOT NULL,
    gender      VARCHAR(20) NOT NULL,
    nationality VARCHAR(40) NOT NULL
);

CREATE TABLE ownerApplication (
    applicationId              SERIAL PRIMARY KEY,
    clientId                   INTEGER      NOT NULL REFERENCES client(id),
    adminId                    INTEGER      REFERENCES admin(id),
    submissionDate             DATE         NOT NULL,
    status                     VARCHAR(50)  NOT NULL,
    propertyAddress            VARCHAR(100) NOT NULL,
    propertyRegistrationNumber VARCHAR(100) NOT NULL
);

CREATE TABLE listing (
    id                   SERIAL PRIMARY KEY,
    ownerId              INTEGER      NOT NULL REFERENCES propertyOwner(id),
    number_of_rooms      INTEGER      NOT NULL CHECK (number_of_rooms > 0),
    number_of_bathrooms  INTEGER      NOT NULL CHECK (number_of_bathrooms > 0),
    has_balcony          BOOLEAN      NOT NULL,
    surface_area         INTEGER      NOT NULL CHECK (surface_area > 0),
    price                INTEGER      NOT NULL CHECK (price >= 0),
    last_Renovated       DATE,
    max_number_of_people INTEGER      NOT NULL CHECK (max_number_of_people > 0),
    country              VARCHAR(100) NOT NULL,
    region               VARCHAR(100) NOT NULL,
    street               VARCHAR(100) NOT NULL,
    room_number          VARCHAR(20)  NOT NULL,
    floor                INTEGER      NOT NULL,
    postal_code          VARCHAR(20)  REFERENCES city(postal_code),
    check_in_time        TIME         NOT NULL DEFAULT '15:00:00',
    check_out_time       TIME         NOT NULL DEFAULT '11:00:00',
    listing_type         VARCHAR(20)  NOT NULL DEFAULT 'SHORT_TERM',
    isBooked             BOOLEAN      DEFAULT FALSE
);

CREATE TABLE Booking (
    clientId        INTEGER NOT NULL REFERENCES client(id),
    listingId       INTEGER NOT NULL REFERENCES listing(id) ON DELETE CASCADE,
    start_Date      DATE,
    end_Date        DATE,
    check_in_time   TIME    NOT NULL,
    check_out_time  TIME    NOT NULL,
    number_of_people INTEGER NOT NULL CHECK (number_of_people > 0),
    PRIMARY KEY (clientId, listingId)
);

CREATE TABLE favorite (
    clientId  INTEGER NOT NULL REFERENCES client(id)  ON DELETE CASCADE,
    listingId INTEGER NOT NULL REFERENCES listing(id) ON DELETE CASCADE,
    PRIMARY KEY (clientId, listingId)
);

CREATE TABLE tenancy_application (
    id                  SERIAL PRIMARY KEY,
    client_id           INTEGER      NOT NULL REFERENCES client(id)  ON DELETE CASCADE,
    listing_id          INTEGER      NOT NULL REFERENCES listing(id) ON DELETE CASCADE,
    has_pets            BOOLEAN      NOT NULL DEFAULT FALSE,
    pets_description    TEXT,
    occupation          VARCHAR(100),
    monthly_income      INTEGER,
    number_of_occupants INTEGER      NOT NULL DEFAULT 1,
    additional_info     TEXT,
    status              VARCHAR(20)  NOT NULL DEFAULT 'pending',
    submitted_at        TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- ---------------------------------------------------------------
-- Seed data
-- ---------------------------------------------------------------

-- Cities
INSERT INTO city (postal_code, name) VALUES
('1000',     'Copenhagen'),
('1054',     'Copenhagen'),
('8700',     'Horsens');

-- Property owners
INSERT INTO propertyOwner
    (first_name, last_name, email, phoneNumber, username, password, dateOfBirth, gender, nationality, numberOfListings)
VALUES
    ('Carlos', 'Mendez', 'carlos.mendez@email.com', '1234567890', 'cmendez',  'pass123',   '1980-05-14', 'Male',   'Spanish',  4),
    ('Aisha',  'Khan',   'aisha.khan@email.com',    '2345678901', 'aishak',   'secure321', '1992-11-22', 'Female', 'Pakistani',2),
    ('Lars',   'Jensen', 'lars.jensen@email.com',   '3456789012', 'ljensen',  'denmark77', '1975-03-09', 'Male',   'Danish',   6),
    ('Yuki',   'Tanaka', 'yuki.tanaka@email.com',   '4567890123', 'yukit',    'tokyo999',  '1988-07-18', 'Female', 'Japanese', 3),
    ('Amara',  'Okafor', 'amara.okafor@email.com',  '5678901234', 'amarao',   'naija22',   '1990-01-30', 'Female', 'Nigerian', 5);

-- Admins
INSERT INTO admin (adminName, username, password) VALUES
    ('Michael Scott',   'mich', 'pass11'),
    ('Sophia Andersen', 'soph', 'pass12'),
    ('Ahmed Hassan',    'ahm',  'pass13');

-- Clients
INSERT INTO client
    (firstName, lastName, email, phoneNumber, username, password, dateOfBirth, gender, nationality)
VALUES
    ('Emily',  'Johnson',  'emily.johnson@email.com',  '6789012345', 'emilyj',  'hello123', '1998-04-12', 'Female', 'American'),
    ('Ravi',   'Patel',    'ravi.patel@email.com',     '7890123456', 'ravip',   'india456', '1995-09-25', 'Male',   'Indian'),
    ('Chen',   'Wei',      'chen.wei@email.com',       '8901234567', 'chenw',   'china789', '2000-02-15', 'Male',   'Chinese'),
    ('Fatima', 'Al-Farsi', 'fatima.farsi@email.com',   '9012345678', 'fatimaf', 'oman321',  '1997-06-20', 'Female', 'Omani'),
    ('Lucas',  'Silva',    'lucas.silva@email.com',    '0123456789', 'lucass',  'brazil11', '1993-12-08', 'Male',   'Brazilian');

-- Owner applications
INSERT INTO ownerApplication
    (clientId, adminId, submissionDate, status, propertyAddress, propertyRegistrationNumber)
VALUES
    (1, 1, '2026-01-10', 'Approved', '12 Green Street, Madrid',    'REG-MAD-001'),
    (2, 2, '2026-02-14', 'Pending',  '45 Lake View, Mumbai',       'REG-MUM-002'),
    (3, 3, '2026-03-05', 'Rejected', '88 Pearl Road, Beijing',     'REG-BEI-003'),
    (4, 1, '2026-03-20', 'Approved', '7 Desert Avenue, Muscat',    'REG-MUS-004');

-- Listings
INSERT INTO listing
    (ownerId, number_of_rooms, number_of_bathrooms, has_balcony, surface_area, price,
     last_Renovated, max_number_of_people, country, region, street, room_number, floor,
     postal_code, check_in_time, check_out_time, listing_type)
VALUES
    (1, 3, 2, TRUE,  95,  6800, '2022-08-15', 4, 'Denmark', 'Copenhagen', 'Niels Juels Gade', '12A', 3, '1054', '15:00:00', '11:00:00', 'SHORT_TERM'),
    (2, 2, 1, TRUE,  71,  6295, '2025-04-03', 2, 'Denmark', 'Horsens',   'Sundvej',           '2',  3, '8700', '15:00:00', '11:00:00', 'LONG_TERM'),
    (3, 4, 2, FALSE, 110, 7500, '2023-06-20', 5, 'Denmark', 'Copenhagen', 'Ameliegade',       '5B', 2, '1000', '14:00:00', '10:00:00', 'SHORT_TERM');

-- Bookings (listing 1 is SHORT_TERM; listing 2 is LONG_TERM and uses tenancy_application)
INSERT INTO Booking
    (clientId, listingId, start_Date, end_Date, check_in_time, check_out_time, number_of_people)
VALUES
    (1, 1, '2026-06-01', '2026-06-07', '15:00:00', '11:00:00', 2);