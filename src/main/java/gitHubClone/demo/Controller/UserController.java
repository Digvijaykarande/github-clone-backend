package gitHubClone.demo.Controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import gitHubClone.demo.dto.ProfileResponse;
import gitHubClone.demo.dto.UpdateProfileRequest;
import gitHubClone.demo.dto.UserPublicProfileResponse;
import gitHubClone.demo.services.UserService;
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ── Own Profile ──────────────────────────────────────────────────────────

    @GetMapping("/me")
    public ProfileResponse getMyProfile(Authentication auth) {
        return userService.getMyProfile(auth.getName());
    }

    @PutMapping("/update")
    public ProfileResponse updateProfile(Authentication auth,
                                         @RequestBody UpdateProfileRequest req) {
        return userService.updateProfile(auth.getName(), req);
    }

    @PostMapping("/profile-image")
    public String uploadImage(@RequestParam MultipartFile file,
                              Principal principal) throws IOException {
        return userService.uploadProfileImage(file, principal.getName());
    }

    // ── Search & Public Profiles ─────────────────────────────────────────────

    /**
     * GET /api/users/search?q=john
     * Case-insensitive partial username search. Returns public-safe profiles.
     */
    @GetMapping("/search")
    public List<UserPublicProfileResponse> searchUsers(@RequestParam String q) {
        return userService.searchByUsername(q);
    }

    /**
     * GET /api/users/{userId}
     * Fetch any user's public profile (no auth needed — public info only).
     */
    @GetMapping("/{userId}")
    public UserPublicProfileResponse getPublicProfile(@PathVariable String userId) {
        return userService.getPublicProfile(userId);
    }

    // ── Follow / Unfollow ────────────────────────────────────────────────────

    @PostMapping("/{userId}/follow")
    public String followUser(@PathVariable String userId, Principal principal) {
        userService.followUser(principal.getName(), userId);
        return "Followed successfully";
    }

    @DeleteMapping("/{userId}/follow")
    public String unfollowUser(@PathVariable String userId, Principal principal) {
        userService.unfollowUser(principal.getName(), userId);
        return "Unfollowed successfully";
    }

    /**
     * GET /api/users/{userId}/followers
     * Returns full public profiles of everyone who follows this user.
     */
    @GetMapping("/{userId}/followers")
    public List<UserPublicProfileResponse> getFollowers(@PathVariable String userId) {
        return userService.getFollowers(userId);
    }

    /**
     * GET /api/users/{userId}/following
     * Returns full public profiles of everyone this user follows.
     */
    @GetMapping("/{userId}/following")
    public List<UserPublicProfileResponse> getFollowing(@PathVariable String userId) {
        return userService.getFollowing(userId);
    }
}