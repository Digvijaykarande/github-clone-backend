package gitHubClone.demo.security;


import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection="users")

public class AppUser {

    @Id
    private String id;

    private String username;
    private String email;
    private String password;

    private String bio;
    private String imageUrl;     
    private List<String> followers;
    private List<String> following;    
    public AppUser() {
	}
	public AppUser(String id, String username, String email, String password, String bio, String imageUrl,
			List<String> followers, List<String> following) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.bio = bio;
		this.imageUrl = imageUrl;
		this.followers = followers;
		this.following = following;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public List<String> getFollowers() {
		return followers;
	}
	public void setFollowers(List<String> followers) {
		this.followers = followers;
	}
	public List<String> getFollowing() {
		return following;
	}
	public void setFollowing(List<String> following) {
		this.following = following;
	}
    

}