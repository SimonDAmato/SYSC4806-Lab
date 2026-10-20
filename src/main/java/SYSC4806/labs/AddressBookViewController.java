package SYSC4806.labs;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class AddressBookViewController {

    private final AddressBookRepository addressBookRepository;

    public AddressBookViewController(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    @GetMapping("/")
    public String listAddressBooks(Model model) {
        model.addAttribute("addressBooks", addressBookRepository.findAll());
        return "addressbooks";
    }

    @GetMapping("/{id}/view")
    public String viewAddressBook(@PathVariable Long id, Model model) {
        AddressBook addressBook = addressBookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("addressBook", addressBook);
        model.addAttribute("buddies", addressBook.getBuddyInfos().values());
        return "addressbook";
    }

    @PostMapping("/{id}/buddies/{name}/delete")
    public String removeBuddy(
            @PathVariable Long id,
            @PathVariable String name) {
        AddressBook addressBook = addressBookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (addressBook.getBuddy(name) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        addressBook.removeBuddy(name);
        addressBookRepository.save(addressBook);
        return "redirect:/{id}/view";
    }

    @PostMapping(value = "/{id}/buddies/create")
    public String addBuddy(
            @PathVariable Long id,
            @ModelAttribute BuddyInfo buddy) {

        if (buddy.getId() != null) {
            buddy.setId(null);
        }

        AddressBook addressBook = addressBookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        addressBook.addBuddy(buddy);
        addressBookRepository.save(addressBook);
        return "redirect:/{id}/view";
    }

    @PostMapping(value = "/create")
    public String addAddressBook() {
        AddressBook addressBook = new AddressBook();
        addressBookRepository.save(addressBook);
        return "redirect:/";
    }

    @PostMapping(value = "/{id}/delete")
    public String removeAddressBook(
            @PathVariable Long id
    ) {
        AddressBook addressBook = addressBookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        addressBookRepository.delete(addressBook);

        return "redirect:/";
    }
}
