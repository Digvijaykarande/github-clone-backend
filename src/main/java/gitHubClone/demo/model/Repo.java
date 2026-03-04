package gitHubClone.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "repositories")
public class Repo {

    @Id
    private String id;

    private String name;
    private String description;
    private String ownerId;
    private String ownerUserName;
    private boolean isPrivate;
    private LocalDateTime createdAt;
    private List<CodeFile> files = new ArrayList<>();
    private List<Commit> commits = new ArrayList<>();

    // Required default constructor
    public Repo() {}

    // Clean constructor for creation
    public Repo(String name, String description,
                String ownerId, String ownerUserName,
                boolean isPrivate) {

        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.ownerUserName = ownerUserName;
        this.isPrivate = isPrivate;
        this.createdAt = LocalDateTime.now();
    }
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerUserName() {
		return ownerUserName;
	}
	public void setOwnerUserName(String ownerUserName) {
		this.ownerUserName = ownerUserName;
	}
	public boolean isPrivate() {
		return isPrivate;
	}
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public List<CodeFile> getFiles() {
		return files;
	}
	public void setFiles(List<CodeFile> files) {
		this.files = files;
	}
	public List<Commit> getCommits() {
	    return commits;
	}
	
}
