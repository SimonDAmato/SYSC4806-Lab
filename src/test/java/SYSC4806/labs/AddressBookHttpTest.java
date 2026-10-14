package SYSC4806.labs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressBookHttpTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AddressBookRepository addressBookRepository;

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

        restTestClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .build();
    }

    @Test
    void ableToGetAddressBook() {
        restTestClient.get()
                .uri("/addressBooks/{id}", addressBook.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$._links.self.href")
                .isEqualTo("http://localhost:%d/addressBooks/%d".formatted(port, addressBook.getId()));
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
}
