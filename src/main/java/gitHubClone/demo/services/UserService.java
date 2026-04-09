package gitHubClone.demo.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String uploadProfileImage(MultipartFile file, String email) throws IOException {

        AppUser user = findByEmailOrThrow(email);

        Map uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap());

        String imageUrl = uploadResult.get("secure_url").toString();
        user.setImageUrl(imageUrl);
        repo.save(user);
        return imageUrl;
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

    public ProfileResponse getMyProfile(String email) {
        return mapToProfileResponse(findByEmailOrThrow(email));
    }

    public ProfileResponse updateProfile(String email, UpdateProfileRequest req) {
        AppUser user = findByEmailOrThrow(email);
        Optional.ofNullable(req.getUsername()).ifPresent(user::setUsername);
        Optional.ofNullable(req.getBio()).ifPresent(user::setBio);
        Optional.ofNullable(req.getImageUrl()).ifPresent(user::setImageUrl);
        repo.save(user);
        return mapToProfileResponse(user);
    }

    // ── Search Users ─────────────────────────────────────────────────────────

    /**
     * Case-insensitive partial search on username.
     * Hits the @Indexed username field — no full collection scan.
     *
     * @throws IllegalArgumentException if query is blank
     */
    public List<UserPublicProfileResponse> searchByUsername(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        return repo.findByUsernameContainingIgnoreCase(query.trim())
                   .stream()
                   .map(this::mapToPublicProfile)
                   .collect(Collectors.toList());
    }

    /**
     * Fetch any user's public profile by their userId.
     */
    public UserPublicProfileResponse getPublicProfile(String userId) {
        AppUser user = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToPublicProfile(user);
    }

    // ── Follow / Unfollow ────────────────────────────────────────────────────

    public void followUser(String myEmail, String targetUserId) {

        AppUser me     = findByEmailOrThrow(myEmail);
        AppUser target = findByIdOrThrow(targetUserId);

        // Bug fix 1: self-follow check must happen AFTER loading both users
        // (so we have me.getId() to compare), but BEFORE any mutation
        if (me.getId().equals(targetUserId)) {
            throw new IllegalArgumentException("You cannot follow yourself");
        }

       
        boolean alreadyFollowing = me.getFollowing().contains(targetUserId);

        if (alreadyFollowing) {
            
            throw new IllegalStateException("You are already following this user");
        }

        me.getFollowing().add(targetUserId);
        target.getFollowers().add(me.getId());

        // Save both AFTER all mutations — keeps following/followers in sync
        repo.save(me);
        repo.save(target);
    }

    public void unfollowUser(String myEmail, String targetUserId) {

        AppUser me     = findByEmailOrThrow(myEmail);
        AppUser target = findByIdOrThrow(targetUserId);

        
        if (!me.getFollowing().contains(targetUserId)) {
            throw new IllegalStateException("You are not following this user");
        }

        // Bug fix 5: self-unfollow guard (edge case but good to block)
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
        if (user.getFollowers() == null || user.getFollowers().isEmpty()) {
            return List.of();
        }
        return repo.findAllById(user.getFollowers())
                   .stream()
                   .map(this::mapToPublicProfile)
                   .collect(Collectors.toList());
    }

    public List<UserPublicProfileResponse> getFollowing(String userId) {
        AppUser user = findByIdOrThrow(userId);
        if (user.getFollowing() == null || user.getFollowing().isEmpty()) {
            return List.of();
        }
        return repo.findAllById(user.getFollowing())
                   .stream()
                   .map(this::mapToPublicProfile)
                   .collect(Collectors.toList());
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
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getBio(),
                user.getImageUrl(),
                user.getFollowers() == null ? 0 : user.getFollowers().size(),
                user.getFollowing() == null ? 0 : user.getFollowing().size()
        );
    }

    private UserPublicProfileResponse mapToPublicProfile(AppUser user) {
        return new UserPublicProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getBio(),
                user.getImageUrl(),
                user.getFollowers() == null ? 0 : user.getFollowers().size(),
                user.getFollowing() == null ? 0 : user.getFollowing().size()
        );
    }
}