package gitHubClone.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import gitHubClone.demo.security.AppUser;

public interface UserRepository extends MongoRepository<AppUser, String> {

    // Used for auth — benefits from the unique @Indexed on email
    Optional<AppUser> findByEmail(String email);

    // Exact match (case-sensitive) — benefits from @Indexed on username
    Optional<AppUser> findByUsername(String username);

    // Partial / case-insensitive search — uses the username index for prefix scans
    List<AppUser> findByUsernameContainingIgnoreCase(String username);
}