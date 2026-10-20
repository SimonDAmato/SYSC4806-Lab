package SYSC4806.labs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureRestTestClient
class AddressBookViewControllerTest {

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private RestTestClient restTestClient;

    private AddressBook addressBook;

    @BeforeEach
    void setUp() {
        addressBookRepository.deleteAll();

        addressBook = new AddressBook();
        addressBook.addBuddy(new BuddyInfo(
                "Alex Stewart",
                "123 Example Street",
                "613-555-0100"));
        addressBook = addressBookRepository.save(addressBook);
    }

    @Test
    void homePageListsAddressBooks() {
        restTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertTrue(body.contains("Address Books")));
    }

    @Test
    void ableToAddBuddyFromAddressBookView() {
        restTestClient.post()
                .uri("/{id}/buddies/create?name={name}&address={address}&phoneNumber={phoneNumber}",
                        addressBook.getId(), "Taylor Stewart", "456 Sample Avenue", "613-555-0101")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FOUND)
                .expectHeader().location("/%d/view".formatted(addressBook.getId()));

        BuddyInfo savedBuddy = addressBookRepository.findById(addressBook.getId())
                .orElseThrow()
                .getBuddy("Taylor Stewart");
        assertNotNull(savedBuddy);
        assertEquals("456 Sample Avenue", savedBuddy.getAddress());
        assertEquals("613-555-0101", savedBuddy.getPhoneNumber());
    }

    @Test
    void ableToRemoveBuddyFromAddressBookView() {
        restTestClient.post()
                .uri("/{id}/buddies/{name}/delete", addressBook.getId(), "Alex Stewart")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FOUND);

        AddressBook savedAddressBook = addressBookRepository.findById(addressBook.getId())
                .orElseThrow();
        assertNull(savedAddressBook.getBuddy("Alex Stewart"));
    }

    @Test
    void ableToAddAddressBookFromView() {
        restTestClient.post()
                .uri("/create")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FOUND);

        assertEquals(2, addressBookRepository.count());
    }

    @Test
    void ableToRemoveAddressBookFromView() {
        restTestClient.post()
                .uri("/{id}/delete", addressBook.getId())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FOUND);

        assertEquals(0, addressBookRepository.count());
    }
}
