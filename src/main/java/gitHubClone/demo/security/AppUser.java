package gitHubClone.demo.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class AppUser {

    @Id
    private String id;

    // Unique index: fast login lookups, enforces no duplicate emails at DB level
    @Indexed(unique = true)
    private String email;

    // Non-unique index: used for search-by-username queries
    @Indexed
    private String username;

    private String password;
    private String bio;
    private String imageUrl;

    private List<String> followers = new ArrayList<>();
    private List<String> following = new ArrayList<>();

    public AppUser() {}

    public String getId()                     { return id; }
    public void   setId(String id)            { this.id = id; }

    public String getEmail()                  { return email; }
    public void   setEmail(String email)      { this.email = email; }

    public String getUsername()               { return username; }
    public void   setUsername(String username){ this.username = username; }

    public String getPassword()               { return password; }
    public void   setPassword(String password){ this.password = password; }

    public String getBio()                    { return bio; }
    public void   setBio(String bio)          { this.bio = bio; }

    public String getImageUrl()               { return imageUrl; }
    public void   setImageUrl(String imageUrl){ this.imageUrl = imageUrl; }

    public List<String> getFollowers()               { return followers; }
    public void         setFollowers(List<String> f) { this.followers = f; }

    public List<String> getFollowing()               { return following; }
    public void         setFollowing(List<String> f) { this.following = f; }
}