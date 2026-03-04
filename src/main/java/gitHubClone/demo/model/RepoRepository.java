package gitHubClone.demo.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepoRepository extends MongoRepository<Repo,String>{
	List<Repo> findByOwnerId(String ownerId);
	Optional<Repo>findByOwnerIdAndName(String ownerId, String name);
	
	List<Repo> findByIsPrivateFalse();
	List<Repo> findByOwnerUserNameAndIsPrivateFalse(String ownerUserName);
	
	List<Repo> findByIsPrivateFalseAndNameContainingIgnoreCase(String name);

	List<Repo> findByIsPrivateFalseAndDescriptionContainingIgnoreCase(String description);

	List<Repo> findByIsPrivateFalseAndOwnerUserNameContainingIgnoreCase(String username);

}
