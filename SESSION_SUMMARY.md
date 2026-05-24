# Session Summary - Room Rental Application

**Date:** May 24, 2026  
**Project:** g6SEP2 - Room Rental Management System

---

## Project Overview

This is a JavaFX-based room rental application using PostgreSQL database with a 3-tier MVC architecture. The application supports three user types: Clients, PropertyOwners, and Admins.

### Technology Stack
- **Java:** Eclipse Adoptium JDK 25.0.3.9-hotspot
- **JavaFX:** Version 26.0.1
  - SDK Path: `C:\Program Files\openjfx-26.0.1_windows-x64_bin-sdk\javafx-sdk-26.0.1\lib`
- **Database:** PostgreSQL 18.4
  - Host: localhost:5432
  - Database: `roomrental`
  - Schema: `sep2`
  - User: `postgres` / Password: `sonim`
- **JDBC Driver:** postgresql-42.7.11.jar (in project root)
- **Image Processing:** Google's libwebp-1.4.0 for WebP to JPG conversion
- **Project Root:** `c:\Users\leond\.vscode\g6SEP2`

---

## Features Implemented in This Session

### 1. **Edit Listing Feature** (PropertyOwner)
- PropertyOwners can edit all listing details including check-in/out times
- Files created/modified:
  - `src/View/EditListingView.fxml` - Edit form UI (based on CreateListingView)
  - `src/View/EditListingController.java` - Pre-populates fields, validates, updates database
  - `src/View/MyListingsController.java` - Added "Edit" button to listing cards
  - `src/Model/Listing.java` - Added missing `setPostalcode()` setter
  - `src/Persistence/ListingDAO.java` - Fixed `updateListing()` method with proper column names and time parameters

### 2. **Cancel Booking Feature** (Client)
- Clients can cancel their bookings at any time
- Files modified:
  - `src/View/MyBookingsController.java` - Added red "Cancel Booking" button to each booking card
  - `src/Persistence/BookingDAO.java` - Fixed `deleteBooking()` to use composite key (clientId, listingId)

### 3. **Delete Listing Feature** (PropertyOwner) ✨ NEW
- PropertyOwners can delete their listings with a confirmation dialog
- Files modified:
  - `src/View/MyListingsController.java` - Added red "Delete" button to each listing card
  - `src/Persistence/ListingDAO.java` - Fixed `deleteListing()` column name bug (`id` not `listingId`)

### 4. **Search & Filter Listings** (Client) ✨ NEW
- Clients can search listings by street/city name
- Filter by maximum price and minimum number of rooms
- "Clear Filters" button resets all filters instantly
- Live filtering as the user types (no need to press a button)
- Files modified:
  - `src/View/BrowseListingsView.fxml` - Added search bar + price/rooms ComboBoxes
  - `src/View/BrowseListingsController.java` - Rewired to load once, then filter in memory

### 5. **Owner Application Flow** (Client → Admin) ✨ NEW
- Clients can apply to become a Property Owner from their home screen
- Admins can review, approve, or reject applications
- On approval: client is copied into the propertyowner table automatically
- Files created:
  - `src/View/OwnerApplicationFormView.fxml` - Application form (address + registration number)
  - `src/View/OwnerApplicationFormController.java` - Submits application to database
  - `src/View/AdminView.fxml` - Admin panel listing all applications
  - `src/View/AdminController.java` - Approve/Reject logic with status badges
- Files modified:
  - `src/Persistence/OwnerApplicationDAO.java` - Added `updateApplicationStatus(int, String)`
  - `src/Persistence/BookingDAO.java` - Added `isListingBooked(int)` for conflict checks
  - `src/View/MainController.java` - Admin nav, Client "Apply as Owner" button, `buildHomeView()`

### 6. **Booking Conflict Check** ✨ NEW
- Prevents booking a listing that was just booked by someone else (race condition guard)
- Files modified:
  - `src/View/BookingDialogController.java` - Checks `isListingBooked()` before submitting

---

## Database Schema Changes

### Listing Table - Check-in/out Times
**File:** `database/add_checkin_checkout_to_listing.sql`

```sql
ALTER TABLE sep2.listing 
ADD COLUMN check_in_time TIME DEFAULT '15:00:00',
ADD COLUMN check_out_time TIME DEFAULT '11:00:00';

UPDATE sep2.listing 
SET check_in_time = '15:00:00', check_out_time = '11:00:00'
WHERE check_in_time IS NULL OR check_out_time IS NULL;

ALTER TABLE sep2.listing
ALTER COLUMN check_in_time SET NOT NULL,
ALTER COLUMN check_out_time SET NOT NULL;
```

**Status:** ✅ Executed by user

### Booking Table Structure
```sql
CREATE TABLE sep2.booking (
    clientId INTEGER NOT NULL,
    listingId INTEGER NOT NULL,
    start_Date DATE,
    end_Date DATE,
    check_in_time TIME NOT NULL,
    check_out_time TIME NOT NULL,
    number_of_people INTEGER NOT NULL CHECK (number_of_people > 0),
    FOREIGN KEY (clientId) REFERENCES client(id),
    FOREIGN KEY (listingId) REFERENCES listing(id),
    PRIMARY KEY (clientId, listingId)  -- COMPOSITE KEY
);
```

**IMPORTANT:** No bookingId column exists - use composite key!

---

## Application Architecture

### Directory Structure
```
g6SEP2/
├── database/
│   ├── Sep2.sql                    # Main schema
│   ├── add_checkin_checkout_to_listing.sql
│   └── sample_test_data.sql
├── room_rental_img/                # Property images (organized by street name)
│   ├── Niels Juels Gade/
│   └── sundvej/
├── src/
│   ├── Model/                      # Entity classes
│   ├── View/                       # FXML and Controllers
│   ├── ViewModel/                  # View Models
│   ├── Persistence/                # DAO classes
│   ├── Network/                    # (Excluded from compilation)
│   ├── Shared/                     # Request/Response/RequestType
│   └── Util/                       # ImageConverter (WebP to JPG)
├── out/                            # Compiled classes
├── postgresql-42.7.11.jar          # JDBC driver
└── SESSION_SUMMARY.md              # This file
```

### Key Files and Their Purpose

#### View Layer
- **Main.java** - Application entry point
- **LoginController.java** - Handles authentication for all user types
- **MainController.java** - Main frame with navigation, routes users to correct views
- **BrowseListingsController.java** - Client view, shows available (unbooked) listings
- **MyBookingsController.java** - Client view, shows bookings with cancel functionality
- **BookingDialogController.java** - Booking form with date/guest inputs
- **MyListingsController.java** - PropertyOwner view with Edit and View buttons
- **CreateListingController.java** - New listing form
- **EditListingController.java** - Edit listing form (pre-populated)
- **ListingDetailController.java** - Modal popup showing full listing details

#### Model Layer
- **Listing.java** - Has checkInTime/checkOutTime (LocalTime), setPostalcode() method
- **Booking.java** - No single ID, uses clientId + listingId
- **Client.java**, **PropertyOwner.java**, **Admin.java** - User types
- **AuthenticationService.java** - Multi-user-type login

#### Persistence Layer
- **ListingDAO.java** - CRUD for listings
  - `updateListing()` - Uses correct column names (number_of_rooms, not numberOfRooms)
  - All SELECT queries load check_in_time and check_out_time
- **BookingDAO.java** - CRUD for bookings
  - `deleteBooking(int clientId, int listingId)` - Uses composite key
  - `getBookingsByClientId(int clientId)` - For My Bookings view
  - `getBookingsByListingId(int listingId)` - For filtering booked listings
- **DatabaseConnection.java** - Connection helper
- All DAOs use `sep2` schema prefix in queries

#### Utilities
- **ImageConverter.java** - Converts WebP to JPG using dwebp.exe, auto-deletes WebP files

---

## How to Compile and Run

### Compilation Command
```powershell
javac --module-path "C:\Program Files\openjfx-26.0.1_windows-x64_bin-sdk\javafx-sdk-26.0.1\lib" `
      --add-modules javafx.controls,javafx.fxml `
      -cp ".;postgresql-42.7.11.jar" `
      -d out `
      src/Persistence/*.java `
      src/Model/Admin.java src/Model/AuthenticationService.java src/Model/Booking.java `
      src/Model/BookingManager.java src/Model/City.java src/Model/Client.java `
      src/Model/Date.java src/Model/Listing.java src/Model/ListingManager.java `
      src/Model/ModelManager.java src/Model/OwnerApplication.java `
      src/Model/OwnerApplicationManager.java src/Model/PropertyOwner.java `
      src/Model/RentalModel.java src/Model/User.java `
      src/Shared/*.java src/Util/*.java src/View/*.java src/ViewModel/*.java
```

### Run Command
```powershell
java --module-path "C:\Program Files\openjfx-26.0.1_windows-x64_bin-sdk\javafx-sdk-26.0.1\lib" `
     --add-modules javafx.controls,javafx.fxml `
     -cp "out;postgresql-42.7.11.jar" `
     View.Main
```

**Note:** Network files (ClientModelManager.java, Server.java, etc.) are excluded due to missing Gson dependency - not needed for current functionality.

---

## Test Credentials

### PropertyOwner Account
- **Username:** cmendez
- **Password:** pass123
- **Database ID:** 1
- **Name:** Carlos Mendez
- **Email:** carlos.mendez@email.com

**Capabilities:**
- View "My Listings" tab
- Create new listings with check-in/out times
- Edit existing listings (all fields including times)
- View listing details

### Client Accounts
Multiple client accounts exist in database - any can book/cancel listings.

**Capabilities:**
- Browse available listings (unbooked only)
- Book listings with date range and guest count
- View "My Bookings" tab
- Cancel bookings with instant UI update

---

## Current Application State

### ✅ Fully Working Features
1. **Authentication System**
   - Multi-user-type login (Client/PropertyOwner/Admin)
   - Auto-detection of user type
   - Session management

2. **Registration System**
   - Form validation
   - Phone number cleaning (removes non-digits)
   - Database insertion

3. **Client Features**
   - Browse available listings (filters out booked properties)
   - **Search by street/city name (live filtering)**
   - **Filter by max price and min rooms**
   - View listing details with images and landlord info
   - Book listings with custom dates and guest count
   - Booking conflict guard (prevents race condition double-booking)
   - View all bookings in "My Bookings" tab
   - Cancel bookings with instant UI removal
   - **Apply as Property Owner from home screen**

4. **PropertyOwner Features**
   - View all owned listings in "My Listings" tab
   - Create new listings with all details + check-in/out times
   - Edit existing listings (all fields)
   - **Delete listings with confirmation dialog**
   - Image upload with file picker
   - WebP to JPG auto-conversion

5. **Admin Features** ✨ NEW
   - View all owner applications with status badges (Pending/Approved/Rejected)
   - Approve applications → auto-creates PropertyOwner from Client data
   - Reject applications with one click
   - Navigate via "Owner Applications" menu item

6. **Image Handling**
   - Auto-converts WebP images to JPG
   - Click-to-enlarge functionality
   - Organized by street name in room_rental_img/

### 🔧 Technical Implementations
- All database queries use `sep2` schema prefix
- Proper handling of composite keys in booking table
- LocalTime for check-in/out times (TIME type in PostgreSQL)
- DatePicker for date inputs
- Modal dialogs for forms and confirmations
- TilePane for card-based layouts
- Immediate UI updates without full reload

---

## Known Issues and Solutions

### Issue 1: Booking Table Has No bookingId Column
**Symptom:** "ERROR: column bookingid does not exist"  
**Cause:** Booking table uses composite primary key (clientId, listingId)  
**Solution:** Use `deleteBooking(int clientId, int listingId)` method

### Issue 2: ListingDAO Column Name Mismatch
**Symptom:** Update queries fail  
**Cause:** Java uses camelCase (numberOfRooms), database uses snake_case (number_of_rooms)  
**Solution:** Always use snake_case in SQL queries

### Issue 3: Missing Postal Code Setter
**Symptom:** Compilation error in EditListingController  
**Cause:** Listing.java was missing setPostalcode() method  
**Solution:** Added method: `public void setPostalcode(String postalcode)`

### Issue 4: WebP Images Not Displaying
**Symptom:** JavaFX doesn't support WebP format  
**Solution:** ImageConverter.java auto-converts to JPG using dwebp.exe

---

## Next Steps / Future Enhancements

### Potential Features to Add
1. **Advanced Booking Features**
   - Email notifications
   - Payment integration
   - Booking history for PropertyOwners

2. **Listing Enhancements**
   - Sorting options (by price, date added)
   - Favorites/wishlist
   - Reviews and ratings
   - Availability calendar

3. **UI/UX Improvements**
   - Loading indicators
   - Better error messages
   - Animations
   - Responsive design

4. **Admin Enhancements**
   - User management (view/disable accounts)
   - Listing moderation
   - System statistics dashboard

---

## Development Notes

### Compilation Warnings (Safe to Ignore)
```
Note: Some input files use unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
WARNING: Restricted methods will be blocked in a future release unless native access is enabled
```

### Database Connection
- Always closes connections in DAO methods
- Uses try-catch blocks for SQLException handling
- Throws RuntimeException on database errors

### Code Style
- FXML files use `fx:controller` attribute
- Controllers use `@FXML` annotations
- DAO methods follow naming: `createX()`, `getXById()`, `updateX()`, `deleteX()`
- All user inputs are validated before database operations

### Image Organization
- Images stored in `room_rental_img/<street_name>/`
- First image in folder is used as listing thumbnail
- WebP files auto-converted to JPG and deleted
- Supported formats: JPG, PNG, (WebP via conversion)

---

## Critical Code Patterns

### Deleting a Booking (Composite Key)
```java
// CORRECT
bookingDAO.deleteBooking(booking.getClientId(), booking.getListingId());

// WRONG - bookingId column doesn't exist
bookingDAO.deleteBooking(booking.getId());
```

### Updating Listing with Times
```java
listing.setCheckInTime(LocalTime.of(15, 0));
listing.setCheckOutTime(LocalTime.of(11, 0));
listingDAO.updateListing(listing);
```

### Filtering Booked Listings
```java
ArrayList<Listing> allListings = listingDAO.getAllListings();
for (Listing listing : allListings) {
    boolean hasBookings = !bookingDAO.getBookingsByListingId(listing.getId()).isEmpty();
    if (!hasBookings) {
        // Show in available listings
    }
}
```

### Immediate UI Update Pattern
```java
// Remove element from UI immediately
parentContainer.getChildren().remove(elementToRemove);

// Then handle database/backend operations
dao.deleteFromDatabase();
```

---

## File Change History (This Session)

### Created Files
1. `src/View/EditListingView.fxml` - Edit listing form UI
2. `src/View/EditListingController.java` - Edit listing logic
3. `SESSION_SUMMARY.md` - This file

### Modified Files
1. `src/Model/Listing.java` - Added setPostalcode() method
2. `src/View/MyListingsController.java` - Added Edit button and handler
3. `src/View/MyBookingsController.java` - Added Cancel button with instant removal
4. `src/Persistence/ListingDAO.java` - Fixed updateListing() column names and times
5. `src/Persistence/BookingDAO.java` - Fixed deleteBooking() to use composite key

### Database Files
1. `database/add_checkin_checkout_to_listing.sql` - ✅ Already executed

---

## End of Session Summary

**Status:** All requested features working perfectly ✅
- Edit Listing: ✅ Working
- Cancel Booking: ✅ Working with instant UI update
- No compilation errors
- No runtime errors
- Database schema up to date

**Last Successful Compilation:** May 24, 2026  
**Application State:** Running and stable

---

*For questions or continuation, refer to this summary and the codebase structure above.*
