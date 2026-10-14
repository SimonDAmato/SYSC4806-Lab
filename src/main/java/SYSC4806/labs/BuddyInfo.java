package SYSC4806.labs;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Represents a contact entry containing basic personal information
 * such as name, address, and phone number.
 *
 * @author Simon D'Amato - 101273839
 */
@Entity
public class BuddyInfo {

    /**
     * Unique primary key identifier for the BuddyInfo entity.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The name of the buddy.
     */
    private String name;

    /**
     * The street address of the buddy.
     */
    private String address;

    /**
     * The phone number of the buddy.
     */
    private String phoneNumber;

    /**
     * Default constructor. Initializes a new empty {@code BuddyInfo} object for
     * JPA.
     */
    public BuddyInfo() {
    }

    /**
     * Constructs a new {@code BuddyInfo} instance with the specified details.
     *
     * @param name        the full name of the buddy
     * @param address     the residential address of the buddy
     * @param phoneNumber the contact phone number of the buddy
     */
    public BuddyInfo(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the database identifier.
     *
     * @return the buddy ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the database identifier.
     *
     * @param id the buddy ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the buddy.
     *
     * @return the name as a {@code String}
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the buddy.
     *
     * @param name the new full name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the residential address of the buddy.
     *
     * @return the address as a {@code String}
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the residential address of the buddy.
     *
     * @param address the new residential address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the phone number of the buddy.
     *
     * @return the phone number as a {@code String}
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the buddy.
     *
     * @param phoneNumber the new contact phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}