package SYSC4806.labs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureRestTestClient
class AddressBookHttpTest {

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
    void ableToGetAddressBook() {
        restTestClient.get()
                .uri("/addressBooks/{id}", addressBook.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$._links.self.href").exists();
    }

    @Test
    void homePageRedirectsToAddressBooks() {
        restTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/addressbooks");
    }

    @Test
    void ableToCreateAddressBook() {
        restTestClient.post()
                .uri("/addressBooks")
                .accept(MediaType.parseMediaType("application/hal+json"))
                .contentType(MediaType.APPLICATION_JSON)
                .body("{}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$._links.self.href").exists();

        assertEquals(2, addressBookRepository.count());
    }

    @Test
    void ableToAddBuddyToAddressBook() {
        restTestClient.post()
                .uri("/api/addressbooks/{id}/buddies", addressBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "name": "Taylor Stewart",
                          "address": "456 Sample Avenue",
                          "phoneNumber": "613-555-0101"
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Taylor Stewart")
                .jsonPath("$.address").isEqualTo("456 Sample Avenue")
                .jsonPath("$.phoneNumber").isEqualTo("613-555-0101");

        BuddyInfo savedBuddy = addressBookRepository.findById(addressBook.getId())
                .orElseThrow()
                .getBuddy("Taylor Stewart");
        assertNotNull(savedBuddy);
        assertEquals("456 Sample Avenue", savedBuddy.getAddress());
        assertEquals("613-555-0101", savedBuddy.getPhoneNumber());
    }

    @Test
    void ableToAddBuddyFromAddressBookView() {
        restTestClient.post()
                .uri("/addressbooks/{id}/buddies/create?name={name}&address={address}&phoneNumber={phoneNumber}",
                        addressBook.getId(), "Taylor Stewart", "456 Sample Avenue", "613-555-0101")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FOUND)
                .expectHeader().location("/addressbooks/%d/view".formatted(addressBook.getId()));

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
                .uri("/addressbooks/{id}/buddies/{name}/delete", addressBook.getId(), "Alex Stewart")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FOUND);

        AddressBook savedAddressBook = addressBookRepository.findById(addressBook.getId())
                .orElseThrow();
        assertNull(savedAddressBook.getBuddy("Alex Stewart"));
    }
}
