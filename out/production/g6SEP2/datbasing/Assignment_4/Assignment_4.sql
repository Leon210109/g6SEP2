CREATE DOMAIN visitor_type_domain AS VARCHAR(10)
    CHECK (VALUE IN ('Standard', 'VIP'));

CREATE DOMAIN artwork_type_domain AS VARCHAR(10)
    CHECK (VALUE IN ('Painting', 'Sculpture', 'Photo'));

CREATE TABLE visitor (
    visitor_id   SERIAL          NOT NULL,
    first_name   VARCHAR(100)    NOT NULL,
    last_name    VARCHAR(100)    NULL,         -- Optional
    visitor_type visitor_type_domain NOT NULL,
    CONSTRAINT pk_visitor PRIMARY KEY (visitor_id)
);

CREATE TABLE gallery (
    gallery_name  VARCHAR(100)  NOT NULL,
    address       VARCHAR(200)  NOT NULL CHECK (LENGTH(address) >= 12),
    opening_time  TIME          NOT NULL,
    closing_time  TIME          NOT NULL,
    CONSTRAINT pk_gallery PRIMARY KEY (gallery_name),
    CONSTRAINT chk_gallery_times CHECK (closing_time > opening_time)
);

CREATE TABLE artist (
    artist_name         VARCHAR(100)  NOT NULL,
    years_of_experience INTEGER       NOT NULL CHECK (years_of_experience >= 0 AND years_of_experience <= 99),
    CONSTRAINT pk_artist PRIMARY KEY (artist_name)
);

CREATE TABLE artwork (
    artwork_id   CHAR(6)              NOT NULL CHECK (LENGTH(artwork_id) = 6),
    title        VARCHAR(40)          NOT NULL CHECK (LENGTH(title) >= 2),
    type         artwork_type_domain  NOT NULL,
    description  VARCHAR(256)         NULL,   -- Optional
    CONSTRAINT pk_artwork PRIMARY KEY (artwork_id)
);

CREATE TABLE exhibition (
    exhibition_id  SERIAL          NOT NULL,
    title          VARCHAR(40)     NOT NULL CHECK (LENGTH(title) >= 2),
    start_date     DATE            NOT NULL,
    end_date       DATE            NOT NULL,
    price          NUMERIC(5, 2)   NOT NULL CHECK (price >= 10.00 AND price <= 99.99),
    gallery_name   VARCHAR(100)    NOT NULL,
    CONSTRAINT pk_exhibition PRIMARY KEY (exhibition_id),
    CONSTRAINT fk_exhibition_gallery
        FOREIGN KEY (gallery_name) REFERENCES gallery(gallery_name),
    CONSTRAINT chk_exhibition_dates CHECK (end_date > start_date)
);

CREATE TABLE ticket (
    visitor_id     INTEGER     NOT NULL,
    exhibition_id  INTEGER     NOT NULL,
    purchase_time  TIMESTAMP   NOT NULL,
    CONSTRAINT pk_ticket PRIMARY KEY (visitor_id, exhibition_id),
    CONSTRAINT fk_ticket_visitor
        FOREIGN KEY (visitor_id)    REFERENCES visitor(visitor_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_ticket_exhibition
        FOREIGN KEY (exhibition_id) REFERENCES exhibition(exhibition_id)
        ON DELETE CASCADE
);

CREATE TABLE exhibition_artwork (
    exhibition_id  INTEGER  NOT NULL,
    artwork_id     CHAR(6)  NOT NULL,
    CONSTRAINT pk_exhibition_artwork PRIMARY KEY (exhibition_id, artwork_id),
    CONSTRAINT fk_ea_exhibition
        FOREIGN KEY (exhibition_id) REFERENCES exhibition(exhibition_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_ea_artwork
        FOREIGN KEY (artwork_id)    REFERENCES artwork(artwork_id)
        ON DELETE RESTRICT
);

CREATE TABLE artwork_artist (
    artist_name  VARCHAR(100)  NULL,   -- nullable to support SET NULL
    artwork_id   CHAR(6)       NOT NULL,
    CONSTRAINT pk_artwork_artist PRIMARY KEY (artwork_id, artist_name),
    CONSTRAINT fk_aa_artist
        FOREIGN KEY (artist_name) REFERENCES artist(artist_name)
        ON DELETE SET NULL,
    CONSTRAINT fk_aa_artwork
        FOREIGN KEY (artwork_id)  REFERENCES artwork(artwork_id)
        ON DELETE CASCADE
);

-- visitor
INSERT INTO visitor (first_name, last_name, visitor_type)
    VALUES ('Alice', 'Johnson', 'Standard');
INSERT INTO visitor (first_name, last_name, visitor_type)
    VALUES ('Bob', NULL, 'VIP');          -- last_name is Optional

-- gallery
INSERT INTO gallery (gallery_name, address, opening_time, closing_time)
    VALUES ('Modern Art House', '12 Art Street, Amsterdam', '09:00', '18:00');
INSERT INTO gallery (gallery_name, address, opening_time, closing_time)
    VALUES ('The Grand Gallery', '45 Museum Lane, Rotterdam', '10:00', '20:00');

-- artist
INSERT INTO artist (artist_name, years_of_experience)
    VALUES ('Leonardo Vinci', 25);
INSERT INTO artist (artist_name, years_of_experience)
    VALUES ('Sofia Reyes', 8);

-- artwork
INSERT INTO artwork (artwork_id, title, type, description)
    VALUES ('AB1C23', 'Starry Bloom', 'Painting', 'An abstract take on Van Gogh style.');
INSERT INTO artwork (artwork_id, title, type, description)
    VALUES ('XY9Z01', 'Stone Figure', 'Sculpture', NULL);  -- description is Optional

-- exhibition  (visitor_id auto-incremented: will be 1 and 2)
INSERT INTO exhibition (title, start_date, end_date, price, gallery_name)
    VALUES ('Spring Showcase', '2026-04-01', '2026-04-30', 15.00, 'Modern Art House');
INSERT INTO exhibition (title, start_date, end_date, price, gallery_name)
    VALUES ('Classic Masters', '2026-05-10', '2026-06-10', 49.99, 'The Grand Gallery');

-- ticket  (visitor_ids 1,2 and exhibition_ids 1,2 generated above)
INSERT INTO ticket (visitor_id, exhibition_id, purchase_time)
    VALUES (1, 1, '2026-03-20 14:30:00');
INSERT INTO ticket (visitor_id, exhibition_id, purchase_time)
    VALUES (2, 2, '2026-03-22 09:15:00');

-- exhibition_artwork
INSERT INTO exhibition_artwork (exhibition_id, artwork_id)
    VALUES (1, 'AB1C23');
INSERT INTO exhibition_artwork (exhibition_id, artwork_id)
    VALUES (2, 'XY9Z01');

-- artwork_artist
INSERT INTO artwork_artist (artist_name, artwork_id)
    VALUES ('Leonardo Vinci', 'AB1C23');
INSERT INTO artwork_artist (artist_name, artwork_id)
    VALUES ('Sofia Reyes', 'XY9Z01');