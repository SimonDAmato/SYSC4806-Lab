package SYSC4806.labs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link BuddyInfo} class.
 *
 * @author Simon D'Amato - 101273839
 */
class BuddyInfoTest {

    private BuddyInfo defaultBuddy;
    private BuddyInfo customBuddy;

    /**
     * Sets up fresh {@code BuddyInfo} instances before each test execution.
     */
    @BeforeEach
    void setUp() {
        defaultBuddy = new BuddyInfo();
        customBuddy = new BuddyInfo("Tom", "100 Generic Street, Ontario Canada", "613-555-0100");
    }

    /**
     * Tests {@link BuddyInfo#getName()} for default and custom constructor
     * instances.
     */
    @Test
    void getName() {
        assertNull(defaultBuddy.getName(), "Name should be null by default.");
        assertEquals("Tom", customBuddy.getName(), "Custom name should match constructor input.");
    }

    /**
     * Tests {@link BuddyInfo#getAddress()} for default and custom constructor
     * instances.
     */
    @Test
    void getAddress() {
        assertNull(defaultBuddy.getAddress(),
                "Default address should be null.");
        assertEquals("100 Generic Street, Ontario Canada", customBuddy.getAddress(),
                "Custom address should match constructor input.");
    }

    /**
     * Tests {@link BuddyInfo#getPhoneNumber()} for default and custom constructor
     * instances.
     */
    @Test
    void getPhoneNumber() {
        assertNull(defaultBuddy.getPhoneNumber(),
                "Default phone number should be null.");
        assertEquals("613-555-0100", customBuddy.getPhoneNumber(),
                "Custom phone number should match constructor input.");
    }
}