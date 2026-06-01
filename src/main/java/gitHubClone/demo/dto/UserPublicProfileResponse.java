package gitHubClone.demo.dto;

import java.io.Serializable;

public class UserPublicProfileResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String bio;
    private String imageUrl;
    private int    followerCount;
    private int    followingCount;

    public UserPublicProfileResponse() {}

    public UserPublicProfileResponse(String id, String username,
                                     String bio, String imageUrl,
                                     int followerCount, int followingCount) {
        this.id             = id;
        this.username       = username;
        this.bio            = bio;
        this.imageUrl       = imageUrl;
        this.followerCount  = followerCount;
        this.followingCount = followingCount;
    }

    public String getId()             { return id; }
    public String getUsername()       { return username; }
    public String getBio()            { return bio; }
    public String getImageUrl()       { return imageUrl; }
    public int    getFollowerCount()  { return followerCount; }
    public int    getFollowingCount() { return followingCount; }
}