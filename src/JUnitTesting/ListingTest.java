package JUnitTesting;

import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;
import Model.*;

 public class ListingTest
{

  private Date makeDate(int day, int month, int year)
  {
    return new Date(day, month, year);
  }

  // Helper to create a basic listing for tests
  private Listing createBasicListing(int id)
  {
    return new Listing(id, "Main St", "Denmark", "Midtjylland", 2, "101", 3, 1,
        true, 80.5f, 1500, makeDate(1, 1, 2020), 10, 4, "8000");
  }

  // ─── ZERO (zero values, empty strings, false booleans) ─────────────────
  @Test void testZero_DefaultValuesAfterConstruction()
  {
    // Using constructor without id (the second constructor)
    Listing listing = new Listing(0, 0, false, 0.0f, 0, makeDate(1, 1, 2000), 0,
        "", "", "", 0, "", "", 0);
    assertEquals(0, listing.getId());
    assertEquals(0, listing.getNumberOfRooms());
    assertEquals(0, listing.getNumberOfBathrooms());
    assertFalse(listing.isBalcony());
    assertEquals(0.0f, listing.getSurfaceArea());
    assertEquals(0, listing.getPrice());
    assertEquals(0, listing.getMaxNumberOfPeople());
    assertFalse(listing.isBooked());  // default is false
    assertEquals(LocalTime.of(15, 0),
        listing.getCheckInTime());  // default 15:00
    assertEquals(LocalTime.of(11, 0),
        listing.getCheckOutTime()); // default 11:00
    assertEquals("SHORT_TERM", listing.getListingType());         // default
  }

  @Test void testZero_EmptyStrings()
  {
    Listing listing = new Listing(1, "", "", "", 0, "", 0, 0, false, 0.0f, 0,
        makeDate(1, 1, 2000), 0, 0, "");
    assertEquals("", listing.getStreet());
    assertEquals("", listing.getCountry());
    assertEquals("", listing.getRegion());
    assertEquals("", listing.getRoomNumber());
    assertEquals("", listing.getPostalcode());
  }

  // ─── ONE (valid listing with all fields) ───────────────────────────────
  @Test void testOne_ValidListingWithId()
  {
    Date renovated = makeDate(15, 6, 2022);
    Listing listing = new Listing(100, "Parkvej 5", "Denmark", "Syddanmark", 1,
        "2A", 4, 2, true, 120.0f, 5000, renovated, 42, 6, "5000");
    assertEquals(100, listing.getId());
    assertEquals("Parkvej 5", listing.getStreet());
    assertEquals("Denmark", listing.getCountry());
    assertEquals("Syddanmark", listing.getRegion());
    assertEquals(1, listing.getFloor());
    assertEquals("2A", listing.getRoomNumber());
    assertEquals(4, listing.getNumberOfRooms());
    assertEquals(2, listing.getNumberOfBathrooms());
    assertTrue(listing.isBalcony());
    assertEquals(120.0f, listing.getSurfaceArea());
    assertEquals(5000, listing.getPrice());
    assertEquals(renovated, listing.getLastRenovated());
    assertEquals(42, listing.getOwnerId());
    assertEquals(6, listing.getMaxNumberOfPeople());
    assertEquals("5000", listing.getPostalcode());
    assertFalse(listing.isBooked()); // default
    assertEquals(LocalTime.of(15, 0), listing.getCheckInTime());
    assertEquals(LocalTime.of(11, 0), listing.getCheckOutTime());
    assertEquals("SHORT_TERM", listing.getListingType());
  }

  @Test void testOne_ValidListingWithoutId()
  {
    Listing listing = new Listing(3, 1, true, 85.5f, 2500, makeDate(1, 1, 2021),
        4, "Denmark", "Hovedstaden", "Vestergade 10", 2, "3", "2100", 10);
    assertEquals(3, listing.getNumberOfRooms());
    assertEquals(1, listing.getNumberOfBathrooms());
    assertTrue(listing.isBalcony());
    assertEquals(85.5f, listing.getSurfaceArea());
    assertEquals(2500, listing.getPrice());
    assertEquals(4, listing.getMaxNumberOfPeople());
    assertEquals("Denmark", listing.getCountry());
    assertEquals("Hovedstaden", listing.getRegion());
    assertEquals("Vestergade 10", listing.getStreet());
    assertEquals(2, listing.getFloor());
    assertEquals("3", listing.getRoomNumber());
    assertEquals("2100", listing.getPostalcode());
    assertEquals(10, listing.getOwnerId());
    // id default 0
    assertEquals(0, listing.getId());
  }

  // ─── MANY (multiple setter calls, multiple listings) ───────────────────
  @Test void testMany_MultipleSetterCalls()
  {
    Listing listing = createBasicListing(1);
    listing.setId(10);
    listing.setId(20);
    listing.setNumberOfRooms(5);
    listing.setNumberOfRooms(7);
    listing.setPrice(1000);
    listing.setPrice(2000);
    assertEquals(20, listing.getId());
    assertEquals(7, listing.getNumberOfRooms());
    assertEquals(2000, listing.getPrice());
  }

  @Test void testMany_MultipleDistinctListings()
  {
    Listing l1 = createBasicListing(1);
    Listing l2 = createBasicListing(2);
    assertNotSame(l1, l2);
    assertNotEquals(l1.getId(), l2.getId());
  }

  // ─── BOUNDARIES (extreme values) ───────────────────────────────────────
  @Test void testBoundaries_MaxMinIntValues()
  {
    Listing listing = createBasicListing(1);
    listing.setId(Integer.MAX_VALUE);
    listing.setNumberOfRooms(Integer.MAX_VALUE);
    listing.setPrice(Integer.MAX_VALUE);
    listing.setMaxNumberOfPeople(Integer.MAX_VALUE);
    listing.setOwnerId(Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE, listing.getId());
    assertEquals(Integer.MAX_VALUE, listing.getNumberOfRooms());
    assertEquals(Integer.MAX_VALUE, listing.getPrice());
    assertEquals(Integer.MAX_VALUE, listing.getMaxNumberOfPeople());
    assertEquals(Integer.MAX_VALUE, listing.getOwnerId());
  }

  @Test void testBoundaries_NegativeValues()
  {
    Listing listing = createBasicListing(1);
    listing.setId(-100);
    listing.setNumberOfRooms(-5);
    listing.setPrice(-1000);
    listing.setMaxNumberOfPeople(-2);
    assertEquals(-100, listing.getId());
    assertEquals(-5, listing.getNumberOfRooms());
    assertEquals(-1000, listing.getPrice());
    assertEquals(-2, listing.getMaxNumberOfPeople());
  }

  @Test void testBoundaries_SurfaceAreaFloat()
  {
    Listing listing = createBasicListing(1);
    listing.setSurfaceArea(Float.MAX_VALUE);
    assertEquals(Float.MAX_VALUE, listing.getSurfaceArea());
    listing.setSurfaceArea(Float.MIN_VALUE);
    assertEquals(Float.MIN_VALUE, listing.getSurfaceArea());
    listing.setSurfaceArea(-999.99f);
    assertEquals(-999.99f, listing.getSurfaceArea());
  }

  @Test void testBoundaries_TimeBoundaries()
  {
    Listing listing = createBasicListing(1);
    listing.setCheckInTime(LocalTime.MIN);
    listing.setCheckOutTime(LocalTime.MAX);
    assertEquals(LocalTime.MIN, listing.getCheckInTime());
    assertEquals(LocalTime.MAX, listing.getCheckOutTime());
  }

  @Test void testBoundaries_DateBoundaries()
  {
    Listing listing = createBasicListing(1);
    Date early = new Date(1, 1, 1);
    Date late = new Date(31, 12, 9999);
    listing.setLastRenovated(early);
    assertEquals(early, listing.getLastRenovated());
    listing.setLastRenovated(late);
    assertEquals(late, listing.getLastRenovated());
  }

  @Test void testBoundaries_VeryLongStrings()
  {
    String longStr = "A".repeat(1000);
    Listing listing = createBasicListing(1);
    listing.setStreet(longStr);
    listing.setCountry(longStr);
    listing.setRegion(longStr);
    listing.setRoomNumber(longStr);
    listing.setPostalcode(longStr);
    assertEquals(longStr, listing.getStreet());
    assertEquals(longStr, listing.getCountry());
    assertEquals(longStr, listing.getRegion());
    assertEquals(longStr, listing.getRoomNumber());
    assertEquals(longStr, listing.getPostalcode());
  }

  // ─── EXCEPTIONS (no validation, so setters don't throw) ────────────────
  @Test void testExceptions_SettersDoNotThrowOnNull()
  {
    Listing listing = createBasicListing(1);
    assertDoesNotThrow(() -> listing.setStreet(null));
    assertDoesNotThrow(() -> listing.setCountry(null));
    assertDoesNotThrow(() -> listing.setRegion(null));
    assertDoesNotThrow(() -> listing.setRoomNumber(null));
    assertDoesNotThrow(() -> listing.setPostalcode(null));
    assertDoesNotThrow(() -> listing.setCheckInTime(null));
    assertDoesNotThrow(() -> listing.setCheckOutTime(null));
    assertDoesNotThrow(() -> listing.setLastRenovated(null));
  }

  @Test void testExceptions_ToStringDoesNotThrowOnNullFields()
  {
    Listing listing = new Listing(0, null, null, null, 0, null, 0, 0, false,
        0.0f, 0, null, 0, 0, null);
    assertDoesNotThrow(listing::toString);
  }
}
