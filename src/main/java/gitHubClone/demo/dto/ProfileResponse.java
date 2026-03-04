package gitHubClone.demo.dto;

public class ProfileResponse {

    private String id;
    private String username;
    private String email;
    private String bio;
    private String imageUrl;
    private int followers;
    private int following;

    public ProfileResponse(String id, String username, String email,
                           String bio, String imageUrl,
                           int followers, int following) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.followers = followers;
        this.following = following;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getBio() { return bio; }
    public String getImageUrl() { return imageUrl; }
    public int getFollowers() { return followers; }
    public int getFollowing() { return following; }
}