package JUnitTesting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import Model.City;
public class TestCity
{
  // ─── ZERO (empty strings, null) ─────────────────────────────────────────
  @Test
  void testZero_EmptyNameAndPostalCode() {
    City city = new City("", "");
    assertEquals("", city.getName());
    assertEquals("", city.getPostalCode());
  }

  @Test
  void testZero_NullNotAllowedByConstructor() {
    // The constructor does NOT check for null, so null can be passed.
    // This is a potential bug. We test the current behavior.
    City city = new City(null, null);
    assertNull(city.getName());
    assertNull(city.getPostalCode());
  }

  // ─── ONE (single valid value) ───────────────────────────────────────────
  @Test
  void testOne_ValidCity() {
    City city = new City("Aarhus", "8000");
    assertEquals("Aarhus", city.getName());
    assertEquals("8000", city.getPostalCode());
  }

  // ─── MANY (not applicable to single object, but can test setter called multiple times)
  @Test
  void testMany_MultipleSetters() {
    City city = new City("", "");
    city.setName("Vejle");
    city.setName("Kolding");  // overwritten
    city.setPostalCode("7100");
    city.setPostalCode("6000");
    assertEquals("Kolding", city.getName());
    assertEquals("6000", city.getPostalCode());
  }

  // ─── BOUNDARIES (max length, min length, special characters)
  @Test
  void testBoundaries_VeryLongName() {
    String longName = "A".repeat(1000);
    City city = new City(longName, "1234");
    assertEquals(longName, city.getName());
  }

  @Test
  void testBoundaries_EmptyPostalCode() {
    City city = new City("Odense", "");
    assertEquals("", city.getPostalCode());
  }

  @Test
  void testBoundaries_SpecialCharactersInName() {
    City city = new City("København Ø", "2100");
    assertEquals("København Ø", city.getName());
  }

  // ─── EXCEPTIONS (no exceptions thrown by City methods, so test that setters don't crash)
  @Test
  void testExceptions_SetterDoesNotThrowOnNull() {
    City city = new City("Aalborg", "9000");
    assertDoesNotThrow(() -> city.setName(null));
    assertDoesNotThrow(() -> city.setPostalCode(null));
  }

  @Test
  void testExceptions_ToStringDoesNotThrowOnNull() {
    City city = new City(null, null);
    assertDoesNotThrow(city::toString);
  }
  }

