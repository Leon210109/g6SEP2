package JUnitTesting;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import Model.*;

class PropertyOwnerTest {

  private Date makeDate(int day, int month, int year) {
    return new Date(day, month, year);
  }

  // ─── ZERO (empty strings, zero ID, nulls) ──────────────────────────────
  @Test
  void testZero_EmptyStringsAndZeroId() {
    PropertyOwner owner = new PropertyOwner("","","","",makeDate(6,6,2012),"","","","",0);
    assertEquals(0, owner.getID());
    assertEquals("", owner.getFirstName());
    assertEquals("", owner.getLastName());
    assertEquals("", owner.getEmail());
    assertEquals("", owner.getPhoneNumber());
    assertEquals("", owner.getUsername());
    assertEquals("", owner.getPassword());
    assertEquals("", owner.getGender());
    assertEquals("", owner.getNationality());
    assertEquals(0, owner.getNumberOflistings());
  }

  @Test
  void testZero_ConstructorWithoutIdWithEmptyStrings() {
    PropertyOwner owner = new PropertyOwner("", "", "", "", "", "", makeDate(1,1,2000), "", "");
    assertEquals("", owner.getFirstName());
    assertEquals("", owner.getLastName());
    assertEquals("", owner.getEmail());
    assertEquals("", owner.getPhoneNumber());
    assertEquals("", owner.getUsername());
    assertEquals("", owner.getPassword());
    assertEquals("", owner.getGender());
    assertEquals("", owner.getNationality());
    assertEquals(0, owner.getID());
    assertEquals(0, owner.getNumberOflistings());
  }

  @Test
  void testZero_NullValuesAllowed() {
    PropertyOwner owner = new PropertyOwner(null, null, null, null, null, null, null, null, null, 0);
    assertNull(owner.getFirstName());
    assertNull(owner.getLastName());
    assertNull(owner.getEmail());
    assertNull(owner.getPhoneNumber());
    assertNull(owner.getUsername());
    assertNull(owner.getPassword());
    assertNull(owner.getDOB());
    assertNull(owner.getGender());
    assertNull(owner.getNationality());
  }

  // ─── ONE (valid property owner) ────────────────────────────────────────
  @Test
  void testOne_ValidOwnerWithId() {
    Date dob = makeDate(10, 10, 1985);
    PropertyOwner owner = new PropertyOwner("Alice", "Brown", "alice@b.com", "777888", dob, "aliceb", "pass99", "Female", "British", 42);
    assertEquals(42, owner.getID());
    assertEquals("Alice", owner.getFirstName());
    assertEquals("Brown", owner.getLastName());
    assertEquals("alice@b.com", owner.getEmail());
    assertEquals("777888", owner.getPhoneNumber());
    assertEquals("aliceb", owner.getUsername());
    assertEquals("pass99", owner.getPassword());
    assertEquals(dob, owner.getDOB());
    assertEquals("Female", owner.getGender());
    assertEquals("British", owner.getNationality());
    assertEquals(0, owner.getNumberOflistings()); // default
  }

  @Test
  void testOne_ValidOwnerWithoutId() {
    Date dob = makeDate(20,5,1990);
    PropertyOwner owner = new PropertyOwner("Bob", "Green", "bob@g.com", "111222", "bobg", "pass55", dob, "Male", "Irish");
    assertEquals(0, owner.getID());
    assertEquals("Bob", owner.getFirstName());
    assertEquals("Green", owner.getLastName());
    assertEquals("bob@g.com", owner.getEmail());
    assertEquals("111222", owner.getPhoneNumber());
    assertEquals("bobg", owner.getUsername());
    assertEquals("pass55", owner.getPassword());
    assertEquals(dob, owner.getDOB());
    assertEquals("Male", owner.getGender());
    assertEquals("Irish", owner.getNationality());
  }

  // ─── MANY (setters: setID, setNumberOflistings, etc.)
  @Test
  void testMany_MultipleSetterCalls() {
    PropertyOwner owner = new PropertyOwner("Old", "Old", "old@x.com", "000" , makeDate(12,12,2004),"","", "M", "X", 0);
    owner.setID(10);
    owner.setID(20);
    owner.setNumberOflistings(5);
    owner.setNumberOflistings(10);
    assertEquals(20, owner.getID());
    assertEquals(10, owner.getNumberOflistings());
  }

  // ─── BOUNDARIES ────────────────────────────────────────────────────────
  @Test
  void testBoundaries_MaxIntId() {
    PropertyOwner owner = new PropertyOwner("", "", "", "", makeDate(12,1,2002), "","", "", "", Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE, owner.getID());
  }

  @Test
  void testBoundaries_NegativeId() {
    PropertyOwner owner = new PropertyOwner("", "", "", "",makeDate(1,1,2000),"", "",  "", "", -999);
    assertEquals(-999, owner.getID());
  }

  @Test
  void testBoundaries_VeryLongStrings() {
    String longStr = "X".repeat(500);
    PropertyOwner owner = new PropertyOwner(longStr, longStr, longStr, longStr, makeDate(12,12,2012), longStr,longStr, longStr, longStr, 1);
    assertEquals(longStr, owner.getFirstName());
    assertEquals(longStr, owner.getLastName());
    assertEquals(longStr, owner.getEmail());
    assertEquals(longStr, owner.getPhoneNumber());
    assertEquals(longStr, owner.getUsername());
    assertEquals(longStr, owner.getPassword());
    assertEquals(longStr, owner.getGender());
    assertEquals(longStr, owner.getNationality());
  }

  // ─── EXCEPTIONS (no validation, so no exceptions thrown)
  @Test
  void testExceptions_ConstructorsDoNotValidateNulls() {
    assertDoesNotThrow(() -> new PropertyOwner(null, null, null, null, null, null, null, null, null, 0));
    assertDoesNotThrow(() -> new PropertyOwner(null, null, null, null, null, null, null, null, null));
  }

  @Test
  void testExceptions_ToStringDoesNotThrowOnNullFields() {
    PropertyOwner owner = new PropertyOwner(null, null, null, null, null, null, null, null, null, 0);
    assertDoesNotThrow(owner::toString);
  }
}
