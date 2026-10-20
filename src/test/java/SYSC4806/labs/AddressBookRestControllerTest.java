package SYSC4806.labs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureRestTestClient
class AddressBookRestControllerTest {

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
                .uri("/{id}", addressBook.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$._links.self.href").exists();
    }

    @Test
    void ableToCreateAddressBook() {
        restTestClient.post()
                .uri("/newAddressBook")
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
                .uri("/api/{id}/buddies", addressBook.getId())
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
}
