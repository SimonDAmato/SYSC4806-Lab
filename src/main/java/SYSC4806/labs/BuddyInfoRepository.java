package SYSC4806.labs;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface BuddyInfoRepository extends CrudRepository<BuddyInfo, Long> {

    Optional<BuddyInfo> findByName(String name);
}
