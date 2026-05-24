# Login & Registration System Implementation

## Overview
The login and registration system has been fully implemented using the existing DAO classes to authenticate users against the PostgreSQL database. The system supports three user types: **Client**, **Property Owner**, and **Admin**.

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
- Navigates to MainView on successful login based on user type
- Handles Enter key in password field
- Supports Admin Access bypass (development mode)
- Navigates to registration view

### 4. **RegistrationController** (`View/RegistrationController.java`) ⭐ NEW
Complete registration functionality:
- Validates all input fields (required fields, email format, password strength)
- Age validation (must be 18+)
- Password confirmation matching
- Saves new clients directly to database using ClientDAO
- Shows success message and auto-redirects to login
- Comprehensive error handling with user-friendly messages

### 5. **RegistrationView.fxml** (`View/RegistrationView.fxml`) ⭐ NEW
Professional registration form with:
- All required client fields (name, email, phone, username, password)
- Date picker for birth date
- Gender dropdown
- Nationality field
- Password confirmation
- Real-time error display
- Responsive two-column layout
- Consistent styling with login view

### 6. **Updated LoginView.fxml** (`View/LoginView.fxml`)
Added:
- Error label for displaying authentication errors
- Proper field IDs for binding
- Register button that navigates to registration

### 7. **Test Utilities**
- **TestAuthentication.java**: Tests database connectivity and authentication
- **TestRegistrationAndLogin.java**: Complete end-to-end test of registration and login ⭐ NEW
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

## How to Use the System

### Registration Flow
1. **Launch the application** (`Main.java`)
2. Click **"Register"** button on login screen
3. **Fill in all required fields**:
   - First Name and Last Name
   - Email (must include @)
   - Phone Number
   - Username (min. 3 characters)
   - Password (min. 6 characters)
   - Confirm Password (must match)
   - Date of Birth (must be 18+)
   - Gender (select from dropdown)
   - Nationality
4. Click **"Create Account"**
5. **Success**: Automatically redirected to login after 2 seconds
6. **Login** with your new credentials

### Login Flow
1. **Enter username and password**
2. Press **Enter** or click **"Login"**
3. **On success**: Redirected to appropriate main view based on user type
   - Client → Client view with bookings and listings
   - Property Owner → Property Owner view with listings management
   - Admin → Admin view
4. **On failure**: Error message displayed

### Admin Access (Development/Testing)
1. Click **"Admin Access ›"** (bottom-right of login screen)
2. Select user type (Client, Property Owner, or Admin)
3. Click **"Enter"** to bypass authentication

---

## How to Test

### Option 1: Full Registration Flow (Recommended)
1. **Start PostgreSQL** and ensure database is running
2. **Run the application**: `Main.java`
3. **Register a new account**:
   - Click "Register"
   - Fill in all fields
   - Click "Create Account"
4. **Login with new credentials**
5. **Verify** you're taken to the correct main view

### Option 2: Using Sample Data
1. **Create test accounts** by running:
   ```sql
   -- Execute in DataGrip: database/sample_test_data.sql
   ```

2. **Launch the application** and try logging in:

   | Username    | Password     | User Type       |
   |-------------|--------------|-----------------|
   | testclient  | password123  | Client          |
   | john_doe    | client123    | Client          |
   | testowner   | password123  | Property Owner  |
   | jane_owner  | owner123     | Property Owner  |
   | testadmin   | admin123     | Admin           |
   | admin       | admin2024    | Admin           |

### Option 3: Run Test Program
```java
// Right-click and run:
TestDao/TestRegistrationAndLogin.java
```
This will:
- Test database connection
- Create a new test user
- Attempt login with new user
- Test login with existing sample users

---

## Features

### ✅ Implemented
- **Database authentication** for all three user types (Client, Property Owner, Admin)
- **Automatic user type detection** - system recognizes user type and loads appropriate UI
- **Client registration** - new users can create accounts that are saved to database
- **Comprehensive validation**:
  - Required fields checking
  - Email format validation
  - Password strength (min. 6 characters)
  - Password confirmation matching
  - Age verification (18+)
  - Username length (min. 3 characters)
- **Real-time error feedback** - clear messages for all validation errors
- **Success confirmation** - visual feedback and auto-redirect after registration
- **Database connection error handling** - graceful error messages
- **Input validation** (empty fields, etc.)
- **Admin bypass** for development/testing
- **Enter key support** in login form

### 🔄 To Be Implemented (Future)
- **Password hashing** (currently passwords are stored in plain text - see Security Notes)
- **Property Owner registration** (separate form with owner-specific fields)
- **Admin registration** (separate form or admin-only feature)
- **Email verification** before account activation
- **Username uniqueness checking** before registration
- **"Remember me"** feature
- **Password reset/recovery**
- **Account lockout** after failed attempts
- **Session management**
- **User profile editing** after login

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
- `src/View/RegistrationController.java` - Registration form controller ⭐
- `src/View/RegistrationView.fxml` - Registration form UI ⭐
- `src/TestDao/TestAuthentication.java` - Database connectivity test
- `src/TestDao/TestRegistrationAndLogin.java` - End-to-end registration & login test ⭐
- `database/sample_test_data.sql` - Test data
- `database/create_database.sql` - Database creation script

### Modified Files:
- `src/View/LoginController.java` - Added login functionality and registration navigation
- `src/ViewModel/LoginViewModel.java` - Added username/password/error properties
- `src/View/LoginView.fxml` - Added error label
- `.vscode/settings.json` - Added PostgreSQL JDBC driver to classpath

---

## Questions or Issues?

If you encounter any problems:
1. Run `TestAuthentication` to diagnose database issues
2. Check the console for error messages
3. Verify PostgreSQL is running and accessible
4. Ensure tables are created with correct schema names
