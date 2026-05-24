-- Add check-in and check-out times to listing table
-- These times will be set by property owners and used for all bookings

ALTER TABLE sep2.listing 
ADD COLUMN check_in_time TIME DEFAULT '15:00:00',
ADD COLUMN check_out_time TIME DEFAULT '11:00:00';

-- Update existing listings with default times
UPDATE sep2.listing
SET check_in_time = '15:00:00',
    check_out_time = '11:00:00'
WHERE check_in_time IS NULL OR check_out_time IS NULL;

-- Make columns NOT NULL after setting defaults
ALTER TABLE sep2.listing
ALTER COLUMN check_in_time SET NOT NULL,
ALTER COLUMN check_out_time SET NOT NULL;

-- Verify the changes
SELECT id, street, check_in_time, check_out_time 
FROM sep2.listing
LIMIT 5;
