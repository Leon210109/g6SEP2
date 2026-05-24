-- Add favorites table for clients to save listings
CREATE TABLE IF NOT EXISTS sep2.favorite (
    clientId  INTEGER NOT NULL,
    listingId INTEGER NOT NULL,
    PRIMARY KEY (clientId, listingId),
    FOREIGN KEY (clientId)  REFERENCES sep2.client(id)  ON DELETE CASCADE,
    FOREIGN KEY (listingId) REFERENCES sep2.listing(id) ON DELETE CASCADE
);
