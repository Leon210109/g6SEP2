package JUnitTesting;

import Model.Admin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAdmin
{
  // ─── ZERO (empty strings, zero ID) ──────────────────────────────────────
  @Test
  void testZero_EmptyStringsInConstructorWithId() {
    Admin admin = new Admin(0, "", "", "");
    assertEquals(0, admin.getID());
    assertEquals("", admin.getAdminName());
    assertEquals("", admin.getUsername());
    assertEquals("", admin.getPassword());
  }

  @Test
  void testZero_EmptyStringsInConstructorWithoutId() {
    Admin admin = new Admin("", "", "");
    assertEquals("", admin.getAdminName());
    assertEquals("", admin.getUsername());
    assertEquals("", admin.getPassword());
    // ID defaults to 0
    assertEquals(0, admin.getID());
  }

  @Test
  void testZero_NullValuesAllowed() {
    Admin admin = new Admin(null, null, null);
    assertNull(admin.getAdminName());
    assertNull(admin.getUsername());
    assertNull(admin.getPassword());
  }

  @Test
  void testZero_DefaultIdIsZero() {
    Admin admin = new Admin("Name", "user", "pass");
    assertEquals(0, admin.getID());
  }

  // ─── ONE (single valid admin with typical values) ───────────────────────
  @Test
  void testOne_ValidAdminWithId() {
    Admin admin = new Admin(5, "John Smith", "john_admin", "secure123");
    assertEquals(5, admin.getID());
    assertEquals("John Smith", admin.getAdminName());
    assertEquals("john_admin", admin.getUsername());
    assertEquals("secure123", admin.getPassword());
  }

  @Test
  void testOne_ValidAdminWithoutId() {
    Admin admin = new Admin("Sarah Lee", "sarah_admin", "pass456");
    assertEquals("Sarah Lee", admin.getAdminName());
    assertEquals("sarah_admin", admin.getUsername());
    assertEquals("pass456", admin.getPassword());
  }

  // ─── MANY (multiple setter calls, overwriting) ──────────────────────────
  @Test
  void testMany_MultipleSetterCalls() {
    Admin admin = new Admin("Old", "old_user", "old_pass");
    admin.setAdminName("New Name");
    admin.setAdminName("Final Name");
    admin.setID(10);
    admin.setID(20);
    assertEquals("Final Name", admin.getAdminName());
    assertEquals(20, admin.getID());
  }

  // ─── BOUNDARIES (extreme values) ────────────────────────────────────────
  @Test
  void testBoundaries_MaxIntId() {
    Admin admin = new Admin(Integer.MAX_VALUE, "Name", "user", "pass");
    assertEquals(Integer.MAX_VALUE, admin.getID());
  }

  @Test
  void testBoundaries_MinIntIdNegative() {
    Admin admin = new Admin(-100, "Name", "user", "pass");
    assertEquals(-100, admin.getID());  // No validation, so negative allowed
  }

  @Test
  void testBoundaries_VeryLongStrings() {
    String longName = "A".repeat(1000);
    String longUsername = "B".repeat(1000);
    String longPassword = "C".repeat(1000);
    Admin admin = new Admin(longName, longUsername, longPassword);
    assertEquals(longName, admin.getAdminName());
    assertEquals(longUsername, admin.getUsername());
    assertEquals(longPassword, admin.getPassword());
  }

  // ─── EXCEPTIONS (methods should not throw, even on bad input) ───────────
  @Test
  void testExceptions_SettersDoNotThrowOnNull() {
    Admin admin = new Admin("x", "x", "x");
    assertDoesNotThrow(() -> admin.setAdminName(null));
    assertDoesNotThrow(() -> admin.setID(100));
    assertDoesNotThrow(() -> admin.setID(-1));
  }

  @Test
  void testExceptions_ToStringDoesNotThrowOnNullFields() {
    Admin admin = new Admin(null, null, null);
    assertDoesNotThrow(admin::toString);
  }

  @Test
  void testExceptions_ToStringWithZeroId() {
    Admin admin = new Admin(0, "", "", "");
    assertDoesNotThrow(admin::toString);
    assertTrue(admin.toString().contains("ID=0"));
  }
}
