package SYSC4806.labs;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AddressBookRestController {

    private final AddressBookRepository addressBookRepository;

    public AddressBookRestController(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<AddressBook> getAddressBook(@PathVariable Long id) {
        AddressBook addressBook = addressBookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return EntityModel.of(
                addressBook,
                WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(AddressBookRestController.class)
                                        .getAddressBook(id))
                        .withSelfRel());
    }

    @PostMapping(value = "/newAddressBook", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AddressBook>> createAddressBook(
            @RequestBody AddressBook addressBook) {
        addressBook.setId(null);
        AddressBook savedAddressBook = addressBookRepository.save(addressBook);
        EntityModel<AddressBook> response = EntityModel.of(
                savedAddressBook,
                WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(AddressBookRestController.class)
                                        .getAddressBook(savedAddressBook.getId()))
                        .withSelfRel());

        return ResponseEntity.created(response.getRequiredLink("self").toUri())
                .body(response);
    }
}
