package SYSC4806.labs;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an address book that stores and manages a collection of
 * {@link BuddyInfo} contacts.
 *
 * @author Simon D'Amato - 101273839
 */
@Entity
public class AddressBook {

    /**
     * Unique primary key identifier for the AddressBook entity.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The collection of contacts in this address book.
     * Uses CascadeType.ALL so persisting, updating, or deleting an AddressBook
     * automatically cascades the operation to its associated BuddyInfo objects.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ADDRESSBOOK_ID")
    @MapKey(name = "name")
    private Map<String, BuddyInfo> buddyInfos;

    /**
     * Default constructor required by JPA. Constructs a new, empty
     * {@code AddressBook}.
     */
    public AddressBook() {
        buddyInfos = new HashMap<>();
    }

    /**
     * Gets the database identifier for this address book.
     *
     * @return the primary key ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the database identifier for this address book.
     *
     * @param id the primary key ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Adds a new buddy contact to the address book using individual details.
     *
     * @param name        the full name of the buddy
     * @param address     the residential address of the buddy
     * @param phoneNumber the contact phone number of the buddy
     */
    public void addBuddy(String name, String address, String phoneNumber) {
        buddyInfos.put(name, new BuddyInfo(name, address, phoneNumber));
    }

    /**
     * Adds an existing {@link BuddyInfo} object directly to the address book.
     *
     * @param buddy the {@code BuddyInfo} object to add
     */
    public void addBuddy(BuddyInfo buddy) {
        buddyInfos.put(buddy.getName(), buddy);
    }

    /**
     * Removes a buddy contact from the address book by their name.
     *
     * @param name the name of the buddy to remove
     */
    public void removeBuddy(String name) {
        buddyInfos.remove(name);
    }

    /**
     * Retrieves a buddy contact from the address book by their name.
     *
     * @param name the name of the buddy to retrieve
     * @return the {@code BuddyInfo} object if found, or {@code null} if not found
     */
    public BuddyInfo getBuddy(String name) {
        return buddyInfos.get(name);
    }

    /**
     * Gets the map of all buddy contacts stored in this address book.
     *
     * @return the map of {@code BuddyInfo} objects
     */
    public Map<String, BuddyInfo> getBuddyInfos() {
        return buddyInfos;
    }

    /**
     * Sets the map of buddy contacts for this address book.
     *
     * @param buddyInfos the map of {@code BuddyInfo} objects to set
     */
    public void setBuddyInfos(Map<String, BuddyInfo> buddyInfos) {
        this.buddyInfos = buddyInfos;
    }

    /**
     * Gets the number of buddies currently in the address book.
     *
     * @return the total number of contacts
     */
    public int size() {
        return buddyInfos.size();
    }

    /**
     * Prints all contacts currently stored in the address book to standard output.
     */
    public void showBuddies() {
        if (buddyInfos.isEmpty()) {
            System.out.println("Address book is empty!");
            return;
        }
        for (BuddyInfo buddy : buddyInfos.values()) {
            System.out.println(buddy.getName() + ": " + buddy.getAddress() + ", " + buddy.getPhoneNumber());
        }
    }

    /**
     * Main method to demonstrate and test basic functionality of the
     * {@code AddressBook}.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Address Book");
        AddressBook addressBook = new AddressBook();

        // Add first buddy
        addressBook.addBuddy("Tom", "100 Generic Street", "000-000-0000");
        System.out.println("\nAfter adding Tom:");
        addressBook.showBuddies();

        // Add second buddy
        addressBook.addBuddy("Tommy", "100 Generic Street", "000-000-0000");
        System.out.println("\nAfter adding Tommy:");
        addressBook.showBuddies();

        // Remove first buddy
        addressBook.removeBuddy("Tom");
        System.out.println("\nAfter removing Tom:");
        addressBook.showBuddies();
    }
}