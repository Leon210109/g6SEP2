
INSERT INTO sep2.client (firstName, lastName, email, phoneNumber, username, password, dateOfBirth, gender, nationality)
VALUES ('Test', 'Client', 'testclient@example.com', '1234567890', 'testclient', 'password123', '1990-01-15', 'Male', 'USA');

-- Test Client 2
INSERT INTO sep2.client (firstName, lastName, email, phoneNumber, username, password, dateOfBirth, gender, nationality)
VALUES ('John', 'Doe', 'john.doe@example.com', '1234567891', 'john_doe', 'client123', '1985-05-20', 'Male', 'Canada');

-- Test Client 3
INSERT INTO sep2.client (firstName, lastName, email, phoneNumber, username, password, dateOfBirth, gender, nationality)
VALUES ('Alice', 'Smith', 'alice.smith@example.com', '1234567892', 'alice', 'alice2024', '1992-08-10', 'Female', 'UK');

-- ========================================
-- Sample Property Owners
-- ========================================

-- Test Owner 1
INSERT INTO sep2.propertyowner (first_name, last_name, email, phoneNumber, username, password, dateOfBirth, gender, nationality, numberOfListings)
VALUES ('Test', 'Owner', 'testowner@example.com', '1234567893', 'testowner', 'password123', '1980-03-25', 'Female', 'Denmark', 0);

-- Test Owner 2
INSERT INTO sep2.propertyowner (first_name, last_name, email, phoneNumber, username, password, dateOfBirth, gender, nationality, numberOfListings)
VALUES ('Jane', 'Property', 'jane.property@example.com', '1234567894', 'jane_owner', 'owner123', '1975-11-30', 'Female', 'Germany', 3);

-- Test Owner 3
INSERT INTO sep2.propertyowner (first_name, last_name, email, phoneNumber, username, password, dateOfBirth, gender, nationality, numberOfListings)
VALUES ('Bob', 'Landlord', 'bob.landlord@example.com', '1234567895', 'bob_owner', 'bob2024', '1988-07-15', 'Male', 'France', 1);

-- ========================================
-- Sample Admins
-- ========================================

-- Test Admin 1
INSERT INTO sep2.admin (adminName, username, password)
VALUES ('Test Admin', 'testadmin', 'admin123');

-- Test Admin 2
INSERT INTO sep2.admin (adminName, username, password)
VALUES ('System Administrator', 'admin', 'admin2024');

-- Test Admin 3
INSERT INTO sep2.admin (adminName, username, password)
VALUES ('Super Admin', 'superadmin', 'super123');

-- ========================================
-- Verification Queries
-- ========================================

-- Check if data was inserted successfully
SELECT 'Clients' as TableName, COUNT(*) as RecordCount FROM sep2.client
UNION ALL
SELECT 'Property Owners', COUNT(*) FROM sep2.propertyowner
UNION ALL
SELECT 'Admins', COUNT(*) FROM sep2.admin;

-- View all test accounts
SELECT 'Client' as UserType, username, password, firstName || ' ' || lastName as FullName FROM sep2.client
UNION ALL
SELECT 'Property Owner', username, password, first_name || ' ' || last_name FROM sep2.propertyowner
UNION ALL
SELECT 'Admin', username, password, adminName FROM sep2.admin
ORDER BY UserType, username;
