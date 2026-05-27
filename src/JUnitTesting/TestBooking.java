package JUnitTesting;

import Model.Booking;
import Model.Date;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class TestBooking
{
  private Date makeDate(int day, int month, int year) {
    return new Date(day, month, year);
  }
  // ─── ZERO (zero values, nulls) ─────────────────────────────────────────
  @Test
  void testZero_AllFieldsZeroOrNull() {
    Booking booking = new Booking(0, 0, null, null, 0, null, null);
    assertEquals(0, booking.getClientId());
    assertEquals(0, booking.getListingId());
    assertNull(booking.getStartDate());
    assertNull(booking.getEndDate());
    assertEquals(0, booking.getNumber_of_people());
    assertNull(booking.getCheck_in_time());
    assertNull(booking.getCheck_out_time());
    assertEquals(0, booking.getId());
  }

  @Test
  void testZero_NullDatesAndTimes() {
    Booking booking = new Booking(1, 2, null, null, 3, null, null);
    assertNull(booking.getStartDate());
    assertNull(booking.getEndDate());
    assertNull(booking.getCheck_in_time());
    assertNull(booking.getCheck_out_time());
  }

  @Test
  void testZero_NumberOfPeopleZero() {
    Booking booking = new Booking(1, 2, makeDate(1,1,2024), makeDate(5,1,2024), 0, LocalTime.of(14,0), LocalTime.of(10,0));
    assertEquals(0, booking.getNumber_of_people());
  }

  // ─── ONE (single valid booking) ─────────────────────────────────────────
  @Test
  void testOne_ValidBooking() {
    Date start = makeDate(1, 6, 2025);
    Date end = makeDate(7, 6, 2025);
    LocalTime checkIn = LocalTime.of(15, 0);
    LocalTime checkOut = LocalTime.of(11, 0);

    Booking booking = new Booking(100, 200, start, end, 2, checkIn, checkOut);

    assertEquals(100, booking.getClientId());
    assertEquals(200, booking.getListingId());
    assertEquals(start, booking.getStartDate());
    assertEquals(end, booking.getEndDate());
    assertEquals(2, booking.getNumber_of_people());
    assertEquals(checkIn, booking.getCheck_in_time());
    assertEquals(checkOut, booking.getCheck_out_time());
  }

  // ─── MANY (multiple setter calls, multiple bookings) ────────────────────
  @Test
  void testMany_MultipleSetterCalls() {
    Date start1 = makeDate(1,1,2024);
    Date end1 = makeDate(5,1,2024);
    Booking booking = new Booking(1, 2, start1, end1, 1, LocalTime.of(14,0), LocalTime.of(10,0));

    Date start2 = makeDate(10,2,2024);
    Date end2 = makeDate(15,2,2024);
    booking.setStartDate(start2);
    booking.setEndDate(end2);
    booking.setNumber_of_people(4);
    booking.setCheck_in_time(LocalTime.of(16, 30));
    booking.setCheck_out_time(LocalTime.of(12, 0));

    assertEquals(start2, booking.getStartDate());
    assertEquals(end2, booking.getEndDate());
    assertEquals(4, booking.getNumber_of_people());
    assertEquals(LocalTime.of(16, 30), booking.getCheck_in_time());
    assertEquals(LocalTime.of(12, 0), booking.getCheck_out_time());
  }

  @Test
  void testMany_MultipleBookingsCreated() {
    Booking b1 = new Booking(1, 10, makeDate(1,1,2024), makeDate(2,1,2024), 1, LocalTime.of(15,0), LocalTime.of(11,0));
    Booking b2 = new Booking(2, 20, makeDate(3,1,2024), makeDate(4,1,2024), 2, LocalTime.of(14,0), LocalTime.of(10,0));
    assertNotSame(b1, b2);
    assertEquals(1, b1.getClientId());
    assertEquals(2, b2.getClientId());
  }

  // ─── BOUNDARIES (edge values for numbers, dates, times) ─────────────────
  @Test
  void testBoundaries_MaxIntValues() {
    Booking booking = new Booking(Integer.MAX_VALUE, Integer.MAX_VALUE,
        makeDate(1,1,2024), makeDate(2,1,2024),
        Integer.MAX_VALUE,
        LocalTime.MAX, LocalTime.MIN);
    assertEquals(Integer.MAX_VALUE, booking.getClientId());
    assertEquals(Integer.MAX_VALUE, booking.getListingId());
    assertEquals(Integer.MAX_VALUE, booking.getNumber_of_people());
  }

  @Test
  void testBoundaries_NegativeIds() {
    Booking booking = new Booking(-1, -5, makeDate(1,1,2024), makeDate(2,1,2024), -10, LocalTime.of(15,0), LocalTime.of(11,0));
    assertEquals(-1, booking.getClientId());
    assertEquals(-5, booking.getListingId());
    assertEquals(-10, booking.getNumber_of_people());
  }

  @Test
  void testBoundaries_DateBoundaries() {
    Date start = makeDate(31, 12, 9999);
    Date end = makeDate(1, 1, 1);
    Booking booking = new Booking(1, 2, start, end, 1, LocalTime.of(23,59), LocalTime.of(0,0));
    assertEquals(start, booking.getStartDate());
    assertEquals(end, booking.getEndDate());
  }

  @Test
  void testBoundaries_TimeBoundaries() {
    LocalTime earliest = LocalTime.MIN;   // 00:00
    LocalTime latest = LocalTime.MAX;     // 23:59:59.999999999
    Booking booking = new Booking(1, 2, makeDate(1,1,2024), makeDate(2,1,2024), 1, earliest, latest);
    assertEquals(earliest, booking.getCheck_in_time());
    assertEquals(latest, booking.getCheck_out_time());
  }

  // ─── EXCEPTIONS (no exceptions thrown by current code, but test that setters don't crash)
  @Test
  void testExceptions_SettersDoNotThrowOnNull() {
    Booking booking = new Booking(1, 2, makeDate(1,1,2024), makeDate(2,1,2024), 1, LocalTime.of(15,0), LocalTime.of(11,0));
    assertDoesNotThrow(() -> booking.setStartDate(null));
    assertDoesNotThrow(() -> booking.setEndDate(null));
    assertDoesNotThrow(() -> booking.setCheck_in_time(null));
    assertDoesNotThrow(() -> booking.setCheck_out_time(null));
    assertDoesNotThrow(() -> booking.setNumber_of_people(-999));
  }

  @Test
  void testExceptions_ToStringDoesNotThrowOnNullFields() {
    Booking booking = new Booking(0, 0, null, null, 0, null, null);
    assertDoesNotThrow(booking::toString);
  }

  @Test
  void testExceptions_ConstructorDoesNotValidate() {
    // No validation in constructor, so these are accepted
    assertDoesNotThrow(() -> new Booking(0, 0, null, null, -100, null, null));
  }
}
