# Login System Implementation

## Overview
The login system has been fully implemented using the existing DAO classes to authenticate users against the PostgreSQL database. The system supports three user types: **Client**, **Property Owner**, and **Admin**.

---

## What Was Implemented

### 1. **AuthenticationService** (`Model/AuthenticationService.java`)
A service class that handles authentication logic:
- Tries to authenticate against all three user tables (Client, PropertyOwner, Admin)
- Returns an `AuthenticationResult` containing:
  - Success status
  - User type (if successful)
  - User object (Client, PropertyOwner, or Admin)
  - Error message (if failed)
- Validates input (checks for empty username/password)
- Handles database connection errors gracefully

### 2. **Enhanced LoginViewModel** (`ViewModel/LoginViewModel.java`)
Extended to support:
- Username and password properties with JavaFX binding
- Error message handling
- User type selection (for Admin Access)
- Field clearing functionality

### 3. **Updated LoginController** (`View/LoginController.java`)
Fully functional login controller:
- Binds UI fields to ViewModel
- Validates user input
- Calls AuthenticationService to verify credentials
- Displays error messages for failed login attempts
- Navigates to MainView on successful login
- Handles Enter key in password field
- Supports Admin Access bypass (development mode)

### 4. **Updated LoginView.fxml** (`View/LoginView.fxml`)
Added:
- Error label for displaying authentication errors
- Proper field IDs for binding

### 5. **Test Utilities**
- **TestAuthentication.java**: Tests database connectivity and authentication
- **sample_test_data.sql**: Sample user accounts for testing

---

## Database Requirements

### Connection Settings
The system expects a PostgreSQL database with these settings (configured in `DatabaseConnection.java`):
- **URL**: `jdbc:postgresql://localhost:5432/roomrental`
- **Username**: `postgres`
- **Password**: `sonim`

### Required Tables
The following tables must exist in the `sep2` schema:
1. **sep2.client** - Stores client user accounts
2. **sep2.propertyowner** - Stores property owner accounts
3. **sep2.admin** - Stores administrator accounts

---

## How to Test

### Option 1: Using Sample Data (Recommended)
1. **Start PostgreSQL server** (ensure it's running on `localhost:5432`)

2. **Create test accounts** by running the SQL script:
   ```sql
   -- Execute: database/sample_test_data.sql
   ```

3. **Test the database connection**:
   ```bash
   # Run from your IDE or command line:
   java TestDao.TestAuthentication
   ```

4. **Launch the application** and try logging in with these credentials:

   | Username    | Password     | User Type       |
   |-------------|--------------|-----------------|
   | testclient  | password123  | Client          |
   | john_doe    | client123    | Client          |
   | testowner   | password123  | Property Owner  |
   | jane_owner  | owner123     | Property Owner  |
   | testadmin   | admin123     | Admin           |
   | admin       | admin2024    | Admin           |

### Option 2: Admin Access (Bypass Login)
If the database is not available:
1. Launch the application
2. Click **"Admin Access ›"** button (bottom-right of login screen)
3. Select user type (Client, Property Owner, or Admin)
4. Click **"Enter"** to access the app without authentication

---

## Features

### ✅ Implemented
- Database authentication for all three user types
- Password validation (stored in plain text - see Security Notes)
- Error handling and user feedback
- Input validation (empty fields, etc.)
- Database connection error handling
- Admin bypass for development/testing

### 🔄 To Be Implemented (Future)
- Password hashing (currently passwords are stored in plain text)
- Registration functionality
- "Remember me" feature
- Password reset/recovery
- Account lockout after failed attempts
- Session management
- User profile loading after login

---

## Security Notes

⚠️ **IMPORTANT**: This implementation stores passwords in **plain text**. This is acceptable for development/testing but **NOT for production**.

### For Production, You Should:
1. Hash passwords using bcrypt or similar:
   ```java
   // Example using BCrypt (you'll need to add the library)
   String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
   boolean matches = BCrypt.checkpw(plainPassword, hashedPassword);
   ```

2. Use prepared statements (already done in DAOs) to prevent SQL injection

3. Implement HTTPS for encrypted communication

4. Add session tokens instead of storing user objects directly

---

## Error Handling

The system handles these error scenarios:

| Scenario | Behavior |
|----------|----------|
| Empty username | Shows: "Please enter a username" |
| Empty password | Shows: "Please enter a password" |
| Invalid credentials | Shows: "Invalid username or password" |
| Database offline | Shows: "Unable to connect to database: [error]" |
| Unexpected error | Shows: "An unexpected error occurred: [error]" |

---

## Architecture

```
User Input (LoginView.fxml)
    ↓
LoginController
    ↓
LoginViewModel (data binding)
    ↓
AuthenticationService
    ↓
ClientDAO / PropertyOwnerDAO / AdminDAO
    ↓
DatabaseConnection
    ↓
PostgreSQL Database
```

---

## Troubleshooting

### Database Connection Issues

**Error**: "Unable to connect to database"

**Solutions**:
1. Verify PostgreSQL is running:
   ```bash
   # Windows:
   Get-Service postgresql*
   
   # Or check if port 5432 is listening:
   netstat -an | findstr 5432
   ```

2. Verify database exists:
   ```sql
   -- In psql or pgAdmin:
   \l  -- List all databases
   ```

3. Check connection settings in `DatabaseConnection.java`:
   - URL: `localhost:5432`
   - Database: `roomrental`
   - Schema: `sep2`
   - Username: `postgres`
   - Password: `sonim`

4. Verify tables exist:
   ```sql
   SELECT table_name FROM information_schema.tables 
   WHERE table_schema = 'sep2';
   ```

### Login Always Fails

1. **Run the test program** to verify database connectivity:
   ```java
   java TestDao.TestAuthentication
   ```

2. **Check if test accounts exist**:
   ```sql
   SELECT username, password FROM sep2.client WHERE username = 'testclient';
   ```

3. **Verify passwords match exactly** (they're case-sensitive)

4. **Check DAO methods** are returning data correctly

---

## Next Steps

To complete the application, you should implement:

1. **Registration System**
   - Create registration forms for each user type
   - Validate email uniqueness
   - Hash passwords before storing

2. **User Session Management**
   - Store logged-in user information
   - Pass user object to ViewModels
   - Implement logout functionality

3. **Profile Management**
   - Allow users to view/edit their profile
   - Update database through DAOs

4. **Password Security**
   - Implement password hashing
   - Add password strength requirements
   - Add "Forgot Password" functionality

---

## File Changes Summary

### New Files Created:
- `src/Model/AuthenticationService.java` - Authentication logic
- `src/TestDao/TestAuthentication.java` - Database connectivity test
- `database/sample_test_data.sql` - Test data

### Modified Files:
- `src/View/LoginController.java` - Added login functionality
- `src/ViewModel/LoginViewModel.java` - Added username/password/error properties
- `src/View/LoginView.fxml` - Added error label

---

## Questions or Issues?

If you encounter any problems:
1. Run `TestAuthentication` to diagnose database issues
2. Check the console for error messages
3. Verify PostgreSQL is running and accessible
4. Ensure tables are created with correct schema names
