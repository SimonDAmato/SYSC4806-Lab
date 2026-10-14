package SYSC4806.labs;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class AddressBookViewController {

    private final AddressBookRepository addressBookRepository;

    public AddressBookViewController(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    @GetMapping("/addressbooks")
    public String listAddressBooks(Model model) {
        model.addAttribute("addressBooks", addressBookRepository.findAll());
        return "addressbooks";
    }

    @GetMapping("/addressbooks/{id}/view")
    public String viewAddressBook(@PathVariable Long id, Model model) {
        AddressBook addressBook = addressBookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("addressBook", addressBook);
        model.addAttribute("buddies", addressBook.getBuddyInfos().values());
        return "addressbook";
    }
}
