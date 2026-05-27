package JUnitTesting;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import Model.*;

class OwnerApplicationTest {

  private Date makeDate(int day, int month, int year) {
    return new Date(day, month, year);
  }

  // ─── ZERO (zero IDs, empty strings, nulls) ─────────────────────────────
  @Test
  void testZero_AllFieldsZeroOrEmpty() {
    Date date = makeDate(0, 0, 0);
    OwnerApplication app = new OwnerApplication(0, 0, 0, date, "", "", "");
    assertEquals(0, app.getApplicationId());
    assertEquals(0, app.getClientId());
    assertEquals(0, app.getAdminId());
    assertEquals(date, app.getSubmissionDate());
    assertEquals("", app.getStatus());
    assertEquals("", app.getPropertyAddress());
    assertEquals("", app.getPropertyRegistrationNumber());
  }

  @Test
  void testZero_ConstructorWithoutAppIdWithEmptyStrings() {
    Date date = makeDate(1, 1, 2024);
    OwnerApplication app = new OwnerApplication(5, 2, date, "", "", "");
    assertEquals(5, app.getClientId());
    assertEquals(2, app.getAdminId());
    assertEquals(date, app.getSubmissionDate());
    assertEquals("", app.getStatus());
    assertEquals("", app.getPropertyAddress());
    assertEquals("", app.getPropertyRegistrationNumber());
  }

  @Test
  void testZero_NullValuesAllowed() {
    OwnerApplication app = new OwnerApplication(0, 0, 0, null, null, null, null);
    assertNull(app.getSubmissionDate());
    assertNull(app.getStatus());
    assertNull(app.getPropertyAddress());
    assertNull(app.getPropertyRegistrationNumber());
  }

  // ─── ONE (valid owner application) ─────────────────────────────────────
  @Test
  void testOne_ValidApplicationWithId() {
    Date date = makeDate(15, 3, 2025);
    OwnerApplication app = new OwnerApplication(101, 42, 1, date, "Pending", "123 Main St", "REG12345");
    assertEquals(101, app.getApplicationId());
    assertEquals(42, app.getClientId());
    assertEquals(1, app.getAdminId());
    assertEquals(date, app.getSubmissionDate());
    assertEquals("Pending", app.getStatus());
    assertEquals("123 Main St", app.getPropertyAddress());
    assertEquals("REG12345", app.getPropertyRegistrationNumber());
  }

  @Test
  void testOne_ValidApplicationWithoutId() {
    Date date = makeDate(10, 10, 2024);
    OwnerApplication app = new OwnerApplication(55, 2, date, "Approved", "Parkvej 5", "REG98765");
    assertEquals(55, app.getClientId());
    assertEquals(2, app.getAdminId());
    assertEquals(date, app.getSubmissionDate());
    assertEquals("Approved", app.getStatus());
    assertEquals("Parkvej 5", app.getPropertyAddress());
    assertEquals("REG98765", app.getPropertyRegistrationNumber());
    // applicationId defaults to 0
    assertEquals(0, app.getApplicationId());
  }

  // ─── MANY (multiple setter calls) ─────────────────────────────────────
  @Test
  void testMany_MultipleSetterCalls() {
    OwnerApplication app = new OwnerApplication(1, 2, makeDate(1,1,2024), "Pending", "Addr", "REG");
    app.setApplicationId(10);
    app.setApplicationId(20);
    app.setStatus("Approved");
    app.setStatus("Rejected");
    app.setPropertyRegistrationNumber("NEW123");
    app.setPropertyRegistrationNumber("NEW456");
    assertEquals(20, app.getApplicationId());
    assertEquals("Rejected", app.getStatus());
    assertEquals("NEW456", app.getPropertyRegistrationNumber());
  }

  // ─── BOUNDARIES (extreme values) ──────────────────────────────────────
  @Test
  void testBoundaries_MaxMinIntIds() {
    Date date = makeDate(1,1,2024);
    OwnerApplication app = new OwnerApplication(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, date, "Status", "Addr", "Reg");
    assertEquals(Integer.MAX_VALUE, app.getApplicationId());
    assertEquals(Integer.MAX_VALUE, app.getClientId());
    assertEquals(Integer.MAX_VALUE, app.getAdminId());

    OwnerApplication appNeg = new OwnerApplication(-100, -200, -300, date, "", "", "");
    assertEquals(-100, appNeg.getApplicationId());
    assertEquals(-200, appNeg.getClientId());
    assertEquals(-300, appNeg.getAdminId());
  }

  @Test
  void testBoundaries_DateBoundaries() {
    Date earliest = new Date(1,1,1);
    Date latest = new Date(31,12,9999);
    OwnerApplication appEarly = new OwnerApplication(0,0,0, earliest, "", "", "");
    OwnerApplication appLate = new OwnerApplication(0,0,0, latest, "", "", "");
    assertEquals(earliest, appEarly.getSubmissionDate());
    assertEquals(latest, appLate.getSubmissionDate());
  }

  @Test
  void testBoundaries_VeryLongStrings() {
    String longStr = "A".repeat(1000);
    OwnerApplication app = new OwnerApplication(0,0,0, makeDate(1,1,2024), longStr, longStr, longStr);
    assertEquals(longStr, app.getStatus());
    assertEquals(longStr, app.getPropertyAddress());
    assertEquals(longStr, app.getPropertyRegistrationNumber());
  }

  // ─── EXCEPTIONS (no validation, so setters don't throw) ────────────────
  @Test
  void testExceptions_SettersDoNotThrowOnNull() {
    OwnerApplication app = new OwnerApplication(1,2, makeDate(1,1,2024), "Pending", "Addr", "Reg");
    assertDoesNotThrow(() -> app.setStatus(null));
    assertDoesNotThrow(() -> app.setPropertyRegistrationNumber(null));
    assertDoesNotThrow(() -> app.setSubmissionDate(null));
  }

  @Test
  void testExceptions_ToStringDoesNotThrowOnNullFields() {
    OwnerApplication app = new OwnerApplication(0,0,0, null, null, null, null);
    assertDoesNotThrow(app::toString);
  }
}

