package JUnitTesting;

import Model.Date;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DateTest
{

  // ─── ZERO (minimum/zero values) ────────────────────────────────────────
  @Test void testZero_AllFieldsZero()
  {
    Date date = new Date(0, 0, 0);
    assertEquals(0, date.getDay());
    assertEquals(0, date.getMonth());
    assertEquals(0, date.getYear());
  }

  // ─── ONE (single valid value) ──────────────────────────────────────────
  @Test void testOne_ValidDate()
  {
    Date date = new Date(12, 8, 2004);
    assertEquals(12, date.getDay());
    assertEquals(8, date.getMonth());
    assertEquals(2004, date.getYear());
  }

  // ─── MANY (multiple setter calls, many different dates) ────────────────
  @Test void testMany_MultipleSetterCalls()
  {
    Date date = new Date(1, 1, 2000);
    date.setDay(15);
    date.setDay(20);
    date.setMonth(6);
    date.setMonth(9);
    date.setYear(2023);
    date.setYear(2025);
    assertEquals(20, date.getDay());
    assertEquals(9, date.getMonth());
    assertEquals(2025, date.getYear());
  }

  @Test void testMany_MultipleDifferentDates()
  {
    Date d1 = new Date(1, 1, 2020);
    Date d2 = new Date(2, 2, 2021);
    Date d3 = new Date(3, 3, 2022);
    assertNotSame(d1, d2);
    assertNotSame(d2, d3);
    assertEquals(1, d1.getDay());
    assertEquals(2, d2.getDay());
    assertEquals(3, d3.getDay());
  }

  // ─── BOUNDARIES (edge values) ──────────────────────────────────────────
  @Test void testBoundaries_MaxValues()
  {
    Date date = new Date(Integer.MAX_VALUE, Integer.MAX_VALUE,
        Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE, date.getDay());
    assertEquals(Integer.MAX_VALUE, date.getMonth());
    assertEquals(Integer.MAX_VALUE, date.getYear());
  }

  @Test void testBoundaries_MinValues()
  {
    Date date = new Date(Integer.MIN_VALUE, Integer.MIN_VALUE,
        Integer.MIN_VALUE);
    assertEquals(Integer.MIN_VALUE, date.getDay());
    assertEquals(Integer.MIN_VALUE, date.getMonth());
    assertEquals(Integer.MIN_VALUE, date.getYear());
  }

  @Test void testBoundaries_ExtremeValidDates()
  {
    // Even unrealistic dates (e.g., day=100) are accepted – no validation
    Date date = new Date(100, 13, -5000);
    assertEquals(100, date.getDay());
    assertEquals(13, date.getMonth());
    assertEquals(-5000, date.getYear());
  }

  // ─── EXCEPTIONS (no exceptions should be thrown) ────────────────────────
  @Test void testExceptions_SettersDoNotThrow()
  {
    Date date = new Date(1, 1, 2000);
    assertDoesNotThrow(() -> date.setDay(100));
    assertDoesNotThrow(() -> date.setMonth(13));
    assertDoesNotThrow(() -> date.setYear(-1000));
    assertDoesNotThrow(() -> date.setDay(Integer.MAX_VALUE));
  }

  @Test void testExceptions_ConstructorDoesNotValidate()
  {
    // Should not throw even with invalid values
    assertDoesNotThrow(() -> new Date(99, 99, 9999));
    assertDoesNotThrow(() -> new Date(-5, -5, -5));
    assertDoesNotThrow(() -> new Date(0, 0, 0));
  }

  @Test void testExceptions_ToStringDoesNotThrow()
  {
    Date date = new Date(100, 100, 100000);
    assertDoesNotThrow(date::toString);
  }
}
