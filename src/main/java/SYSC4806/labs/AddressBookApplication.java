package SYSC4806.labs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AddressBookApplication {

    private static final Logger logger = LoggerFactory.getLogger(AddressBookApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AddressBookApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(
            BuddyInfoRepository buddyInfoRepository,
            AddressBookRepository addressBookRepository) {

        return (args) -> {

            // Save a few buddies
            BuddyInfo tom = new BuddyInfo(
                    "Tom",
                    "100 Generic Street",
                    "000-000-0000");

            BuddyInfo john = new BuddyInfo(
                    "John",
                    "200 Main Street",
                    "111-111-1111");

            buddyInfoRepository.save(tom);
            buddyInfoRepository.save(john);

            // Fetch all buddies
            logger.info("");
            logger.info("Buddies found with findAll():");
            logger.info("-------------------------------");

            buddyInfoRepository.findAll().forEach(buddy -> {
                logger.info(
                        buddy.getId() + ": "
                                + buddy.getName() + ", "
                                + buddy.getAddress() + ", "
                                + buddy.getPhoneNumber());
            });

            logger.info("");

            // Find a buddy by name
            logger.info("Buddy found with findByName('Tom'):");
            logger.info("----------------------------------");

            buddyInfoRepository.findByName("Tom").ifPresent(buddy -> {
                logger.info(
                        buddy.getId() + ": "
                                + buddy.getName() + ", "
                                + buddy.getAddress() + ", "
                                + buddy.getPhoneNumber());
            });

            logger.info("");

            // Create an AddressBook
            AddressBook addressBook = new AddressBook();

            addressBook.addBuddy(
                    "Timmy",
                    "101 Generic Street",
                    "000-000-0001");

            addressBook.addBuddy(
                    "Jimmy",
                    "202 Main Street",
                    "111-111-1112");

            // Save the AddressBook
            AddressBook savedAddressBook = addressBookRepository.save(addressBook);

            logger.info("AddressBook saved with ID: "
                    + savedAddressBook.getId());

            logger.info("");

            // Find AddressBook by ID
            logger.info("AddressBook found with findById():");
            logger.info("----------------------------------");

            addressBookRepository
                    .findById(savedAddressBook.getId())
                    .ifPresent(foundBook -> {
                        logger.info(
                                "AddressBook ID: "
                                        + foundBook.getId());

                        logger.info(
                                "Number of buddies: "
                                        + foundBook.size());

                        foundBook.getBuddyInfos()
                                .values()
                                .forEach(buddy -> {
                                    logger.info(
                                            buddy.getName() + ": "
                                                    + buddy.getAddress()
                                                    + ", "
                                                    + buddy.getPhoneNumber());
                                });
                    });
            logger.info("");
        };
    }
}
