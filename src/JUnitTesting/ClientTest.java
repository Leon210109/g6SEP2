package JUnitTesting;

import Model.Client;
import Model.Date;
import Model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

  // Helper to create a Date
  private Date makeDate(int day, int month, int year) {
    return new Date(day, month, year);
  }

  // ─── ZERO (empty strings, zero IDs, nulls) ──────────────────────────────
  @Test
  void testZero_EmptyStringsAndZeroId() {
    Client client = new Client(0, "", "", "", "", "", "", makeDate(1,1,2000), "", "");
    assertEquals(0, client.getID());
    assertEquals("", client.getFirstName());
    assertEquals("", client.getLastName());
    assertEquals("", client.getEmail());
    assertEquals("", client.getPhoneNumber());
    assertEquals("", client.getUsername());
    assertEquals("", client.getPassword());
    assertEquals("", client.getGender());
    assertEquals("", client.getNationality());
    assertNotNull(client.getDOB());
  }

  @Test
  void testZero_ConstructorWithoutIdWithEmptyStrings() {
    Client client = new Client("", "", "", "", "", "", makeDate(1,1,2000), "", "");
    assertEquals("", client.getFirstName());
    assertEquals("", client.getLastName());
    assertEquals("", client.getEmail());
    assertEquals("", client.getPhoneNumber());
    assertEquals("", client.getUsername());
    assertEquals("", client.getPassword());
    assertEquals("", client.getGender());
    assertEquals("", client.getNationality());
  }

  @Test
  void testZero_NullValuesAllowed() {
    Client client = new Client(0, null, null, null, null, null, null, null, null, null);
    assertNull(client.getFirstName());
    assertNull(client.getLastName());
    assertNull(client.getEmail());
    assertNull(client.getPhoneNumber());
    assertNull(client.getUsername());
    assertNull(client.getPassword());
    assertNull(client.getDOB());
    assertNull(client.getGender());
    assertNull(client.getNationality());
  }

  // ─── ONE (valid client with all fields) ─────────────────────────────────
  @Test
  void testOne_ValidClientWithId() {
    Date dob = makeDate(15, 5, 1990);
    Client client = new Client(101, "John", "Doe", "john@example.com", "5551234", "johnd", "pass123", dob, "Male", "American");
    assertEquals(101, client.getID());
    assertEquals("John", client.getFirstName());
    assertEquals("Doe", client.getLastName());
    assertEquals("john@example.com", client.getEmail());
    assertEquals("5551234", client.getPhoneNumber());
    assertEquals("johnd", client.getUsername());
    assertEquals("pass123", client.getPassword());
    assertEquals(dob, client.getDOB());
    assertEquals("Male", client.getGender());
    assertEquals("American", client.getNationality());
  }

  @Test
  void testOne_ValidClientWithoutId() {
    Date dob = makeDate(20, 8, 1995);
    Client client = new Client("Jane", "Smith", "jane@example.com", "5555678", "janes", "pass456", dob, "Female", "Canadian");
    assertEquals(0, client.getID()); // ID defaults to 0
    assertEquals("Jane", client.getFirstName());
    assertEquals("Smith", client.getLastName());
    assertEquals("jane@example.com", client.getEmail());
    assertEquals("5555678", client.getPhoneNumber());
    assertEquals("janes", client.getUsername());
    assertEquals("pass456", client.getPassword());
    assertEquals(dob, client.getDOB());
    assertEquals("Female", client.getGender());
    assertEquals("Canadian", client.getNationality());
  }

  // ─── MANY (multiple setters? Note: Client has no setters except ID? Actually no setters except ID? Let's check Client.java - it has no setters for most fields. Only getters. So "many" tests are limited.)
  @Test
  void testMany_MultipleClientsDistinct() {
    Client c1 = new Client(1, "A", "B", "a@b.com", "111", "u1", "p1", makeDate(1,1,2000), "M", "X");
    Client c2 = new Client(2, "C", "D", "c@d.com", "222", "u2", "p2", makeDate(2,2,2000), "F", "Y");
    assertNotSame(c1, c2);
    assertNotEquals(c1.getID(), c2.getID());
  }

  // ─── BOUNDARIES (extreme values for strings and numbers) ────────────────
  @Test
  void testBoundaries_MaxIntId() {
    Client client = new Client(Integer.MAX_VALUE, "", "", "", "", "", "", makeDate(1,1,2000), "", "");
    assertEquals(Integer.MAX_VALUE, client.getID());
  }

  @Test
  void testBoundaries_NegativeId() {
    Client client = new Client(-999, "", "", "", "", "", "", makeDate(1,1,2000), "", "");
    assertEquals(-999, client.getID());
  }

  @Test
  void testBoundaries_VeryLongStrings() {
    String longStr = "A".repeat(1000);
    Client client = new Client(1, longStr, longStr, longStr, longStr, longStr, longStr, makeDate(1,1,2000), longStr, longStr);
    assertEquals(longStr, client.getFirstName());
    assertEquals(longStr, client.getLastName());
    assertEquals(longStr, client.getEmail());
    assertEquals(longStr, client.getPhoneNumber());
    assertEquals(longStr, client.getUsername());
    assertEquals(longStr, client.getPassword());
    assertEquals(longStr, client.getGender());
    assertEquals(longStr, client.getNationality());
  }

  @Test
  void testBoundaries_DateBoundaries() {
    Date earliest = new Date(1, 1, 1);
    Date latest = new Date(31, 12, 9999);
    Client clientEarliest = new Client(1, "", "", "", "", "", "", earliest, "", "");
    Client clientLatest = new Client(2, "", "", "", "", "", "", latest, "", "");
    assertEquals(earliest, clientEarliest.getDOB());
    assertEquals(latest, clientLatest.getDOB());
  }

  // ─── EXCEPTIONS (no validation, so no exceptions thrown) ────────────────
  @Test
  void testExceptions_ConstructorDoesNotValidateNulls() {
    assertDoesNotThrow(() -> new Client(0, null, null, null, null, null, null, null, null, null));
  }

  @Test
  void testExceptions_ToStringDoesNotThrowOnNullFields() {
    Client client = new Client(0, null, null, null, null, null, null, null, null, null);
    assertDoesNotThrow(client::toString);
  }

  @Test
  void testExceptions_GetUserMethodDoesNotThrow() {
    Client client = new Client(
        1, "John", "Doe", "john@x.com", "555", "john_user", "pass123",
        new Date(1,1,2000), "M", "USA"
    );
    assertDoesNotThrow(client::getUser);
    User user = client.getUser();
    assertEquals("john_user", user.getUsername());
    assertEquals("pass123", user.getPassword());

  }
}