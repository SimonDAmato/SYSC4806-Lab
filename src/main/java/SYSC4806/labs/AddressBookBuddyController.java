package SYSC4806.labs;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class AddressBookBuddyController {

    private final AddressBookRepository addressBookRepository;

    public AddressBookBuddyController(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    @PostMapping(
            value = "/{id}/buddies",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public BuddyInfo addBuddy(
            @PathVariable Long id,
            @RequestBody BuddyInfo buddy) {
        if (buddy.getId() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Buddy ID is assigned by the server");
        }

        AddressBook addressBook = addressBookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        addressBook.addBuddy(buddy);
        AddressBook savedAddressBook = addressBookRepository.save(addressBook);
        return savedAddressBook.getBuddy(buddy.getName());
    }
}
