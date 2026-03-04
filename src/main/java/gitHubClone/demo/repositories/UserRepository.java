package gitHubClone.demo.repositories;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import gitHubClone.demo.security.AppUser;

public interface UserRepository extends MongoRepository<AppUser,String> {

    Optional<AppUser> findByEmail(String email);

}