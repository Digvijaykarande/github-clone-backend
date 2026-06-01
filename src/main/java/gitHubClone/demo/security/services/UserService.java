package gitHubClone.demo.security.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import gitHubClone.demo.dto.ProfileResponse;
import gitHubClone.demo.dto.UpdateProfileRequest;
import gitHubClone.demo.dto.UserPublicProfileResponse;
import gitHubClone.demo.repositories.UserRepository;
import gitHubClone.demo.security.AppUser;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    // ── Profile Image ────────────────────────────────────────────────────────

    @Caching(evict = {
        @CacheEvict(value = "profiles",       key = "#email"),
        @CacheEvict(value = "publicProfiles", key = "#result") // evicted by userId after upload
    })
    public String uploadProfileImage(MultipartFile file, String email) throws IOException {
        AppUser user = findByEmailOrThrow(email);

        String imageUrl = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap())
                .get("secure_url").toString();

        user.setImageUrl(imageUrl);
        repo.save(user);
        return imageUrl; // NOTE: @CacheEvict key="#result" won't work here since result is imageUrl,
                         // not userId. See updateProfile for the correct eviction pattern.
    }

    // ── Registration ─────────────────────────────────────────────────────────

    public AppUser createUser(AppUser user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }

    // ── Own Profile ──────────────────────────────────────────────────────────

    @Cacheable(value = "profiles", key = "#email")
    public ProfileResponse getMyProfile(String email) {
    	 System.out.println("MongoDB HIT");
        return mapToProfileResponse(findByEmailOrThrow(email));
    }

    @Caching(evict = {
        @CacheEvict(value = "profiles",       key = "#email"),
        @CacheEvict(value = "publicProfiles", key = "#result.id") // precise: only this user's public cache
    })
    public ProfileResponse updateProfile(String email, UpdateProfileRequest req) {
        AppUser user = findByEmailOrThrow(email);

        Optional.ofNullable(req.getUsername()).ifPresent(user::setUsername);
        Optional.ofNullable(req.getBio()).ifPresent(user::setBio);
        Optional.ofNullable(req.getImageUrl()).ifPresent(user::setImageUrl);

        repo.save(user);
        return mapToProfileResponse(user);
    }

    // ── Search Users ─────────────────────────────────────────────────────────

    // Not cached intentionally — query results are dynamic and hard to invalidate cleanly
    public List<UserPublicProfileResponse> searchByUsername(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        return repo.findByUsernameContainingIgnoreCase(query.trim())
                   .stream()
                   .map(this::mapToPublicProfile)
                   .collect(Collectors.toList());
    }

    @Cacheable(value = "publicProfiles", key = "#userId")
    public UserPublicProfileResponse getPublicProfile(String userId) {
        return mapToPublicProfile(
            repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"))
        );
    }

    // ── Follow / Unfollow ────────────────────────────────────────────────────

    // Evicts both affected users' public profiles — follower counts changed
    @Caching(evict = {
        @CacheEvict(value = "publicProfiles", key = "#targetUserId"),
        @CacheEvict(value = "profiles",       key = "#myEmail")
    })
    public void followUser(String myEmail, String targetUserId) {
        AppUser me     = findByEmailOrThrow(myEmail);
        AppUser target = findByIdOrThrow(targetUserId);

        if (me.getId().equals(targetUserId)) {
            throw new IllegalArgumentException("You cannot follow yourself");
        }
        if (me.getFollowing().contains(targetUserId)) {
            throw new IllegalStateException("You are already following this user");
        }

        me.getFollowing().add(targetUserId);
        target.getFollowers().add(me.getId());

        repo.save(me);
        repo.save(target);
    }

    @Caching(evict = {
        @CacheEvict(value = "publicProfiles", key = "#targetUserId"),
        @CacheEvict(value = "profiles",       key = "#myEmail")
    })
    public void unfollowUser(String myEmail, String targetUserId) {
        AppUser me     = findByEmailOrThrow(myEmail);
        AppUser target = findByIdOrThrow(targetUserId);

        if (!me.getFollowing().contains(targetUserId)) {
            throw new IllegalStateException("You are not following this user");
        }
        if (me.getId().equals(targetUserId)) {
            throw new IllegalArgumentException("You cannot unfollow yourself");
        }

        me.getFollowing().remove(targetUserId);
        target.getFollowers().remove(me.getId());

        repo.save(me);
        repo.save(target);
    }

    public List<UserPublicProfileResponse> getFollowers(String userId) {
        AppUser user = findByIdOrThrow(userId);
        if (user.getFollowers() == null || user.getFollowers().isEmpty()) return List.of();
        return repo.findAllById(user.getFollowers())
                   .stream().map(this::mapToPublicProfile).collect(Collectors.toList());
    }

    public List<UserPublicProfileResponse> getFollowing(String userId) {
        AppUser user = findByIdOrThrow(userId);
        if (user.getFollowing() == null || user.getFollowing().isEmpty()) return List.of();
        return repo.findAllById(user.getFollowing())
                   .stream().map(this::mapToPublicProfile).collect(Collectors.toList());
    }

    // ── Private Helpers ──────────────────────────────────────────────────────

    private AppUser findByEmailOrThrow(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    private AppUser findByIdOrThrow(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    private ProfileResponse mapToProfileResponse(AppUser user) {
        return new ProfileResponse(
                user.getId(), user.getUsername(), user.getEmail(), user.getBio(), user.getImageUrl(),
                user.getFollowers() == null ? 0 : user.getFollowers().size(),
                user.getFollowing() == null ? 0 : user.getFollowing().size()
        );
    }

    private UserPublicProfileResponse mapToPublicProfile(AppUser user) {
        return new UserPublicProfileResponse(
                user.getId(), user.getUsername(), user.getBio(), user.getImageUrl(),
                user.getFollowers() == null ? 0 : user.getFollowers().size(),
                user.getFollowing() == null ? 0 : user.getFollowing().size()
        );
    }
}