package JUnitTesting;
import Model.City;
import Model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class TestUser
{
  // ZERO
  @Test
  void testZero_EmptyStrings() {
    User user = new User("", "");
    assertEquals("", user.getUsername());
    assertEquals("", user.getPassword());
  }

  @Test
  void testZero_NullValues() {
    User user = new User(null, null);
    assertNull(user.getUsername());
    assertNull(user.getPassword());
  }

  // ONE
  @Test
  void testOne_ValidUser() {
    User user = new User("john", "pass123");
    assertEquals("john", user.getUsername());
    assertEquals("pass123", user.getPassword());
  }

  // BOUNDARIES
  @Test
  void testBoundaries_MaxLengthUsername() {
    String longName = "A".repeat(255);
    User user = new User(longName, "pwd");
    assertEquals(longName, user.getUsername());
  }

  // EXCEPTIONS
  @Test
  void testExceptions_SettersAllowNull() {
    User user = new User("a", "b");
    assertDoesNotThrow(() -> user.setUsername(null));
    assertDoesNotThrow(() -> user.setPassword(null));
  }
}

