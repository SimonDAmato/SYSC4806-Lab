package SYSC4806.labs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link AddressBook} class.
 *
 * @author Simon D'Amato - 101273839
 */
class AddressBookTest {

    private AddressBook addressBook;

    /**
     * Sets up a fresh {@code AddressBook} instance before each test.
     */
    @BeforeEach
    void setUp() {
        addressBook = new AddressBook();
    }

    /**
     * Tests adding a buddy to the address book and verifies the details are
     * properly set
     * by querying the address book's state.
     */
    @Test
    void testAddBuddy() {
        addressBook.addBuddy("Tom", "100 Generic Street", "613-555-0100");

        assertEquals(1, addressBook.size(), "Address book should contain exactly 1 buddy.");

        BuddyInfo buddy = addressBook.getBuddy("Tom");
        assertNotNull(buddy, "Buddy 'Tom' should exist in the address book.");
        assertEquals("Tom", buddy.getName(), "Buddy name should match.");
        assertEquals("100 Generic Street", buddy.getAddress(), "Buddy address should match.");
        assertEquals("613-555-0100", buddy.getPhoneNumber(), "Buddy phone number should match.");
    }

    /**
     * Tests removing an existing buddy from the address book and verifies the state
     * updates.
     */
    @Test
    void testRemoveBuddy() {
        addressBook.addBuddy("Tom", "100 Generic Street", "613-555-0100");
        assertEquals(1, addressBook.size(), "Address book should contain 1 buddy before removal.");

        addressBook.removeBuddy("Tom");

        assertEquals(0, addressBook.size(), "Address book should be empty after removal.");
        assertNull(addressBook.getBuddy("Tom"), "Buddy 'Tom' should no longer exist in the address book.");
    }

    /**
     * Tests printing output when the address book is empty vs. populated.
     */
    @Test
    void testShowBuddies() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        addressBook.showBuddies();
        assertTrue(outContent.toString().contains("Address book is empty!"));

        outContent.reset();
        addressBook.addBuddy("Tommy", "200 Generic Street", "613-555-0200");
        addressBook.showBuddies();

        assertTrue(outContent.toString().contains("Tommy: 200 Generic Street, 613-555-0200"));
    }
}