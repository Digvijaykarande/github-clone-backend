package gitHubClone.demo.services;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import gitHubClone.demo.dto.ProfileResponse;
import gitHubClone.demo.dto.UpdateProfileRequest;
import gitHubClone.demo.repositories.UserRepository;
import gitHubClone.demo.security.AppUser;
import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private Cloudinary cloudinary;

    public String uploadProfileImage(MultipartFile file, String email)
            throws IOException {

        AppUser user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap());

        String imageUrl = uploadResult.get("secure_url").toString();

        user.setImageUrl(imageUrl);

        repo.save(user);

        return imageUrl;
    }

    // ✅ Registration
    public AppUser createUser(AppUser user) {

        // 🔥 Prevent duplicate email
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repo.save(user);
    }

    // ✅ Get Profile
    public ProfileResponse getMyProfile(String email) {

        AppUser user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        return mapToProfileResponse(user);
    }

    // ✅ Update Profile
    public ProfileResponse updateProfile(String email, UpdateProfileRequest req) {

        AppUser user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        // Only update if value is provided
        Optional.ofNullable(req.getUsername()).ifPresent(user::setUsername);
        Optional.ofNullable(req.getBio()).ifPresent(user::setBio);
        Optional.ofNullable(req.getImageUrl()).ifPresent(user::setImageUrl);

        repo.save(user);

        return mapToProfileResponse(user);
    }

    // ✅ Private Mapper (Clean Code Practice)
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
    
    //follow & followings
    public void followUser(String myEmail, String targetUserId) {

        AppUser me = repo.findByEmail(myEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AppUser target = repo.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        if(me.getId().equals(targetUserId))
            throw new RuntimeException("You cannot follow yourself");

        if (me.getFollowing() == null) {
            me.setFollowing(new ArrayList<>());
        }

        if (target.getFollowers() == null) {
            target.setFollowers(new ArrayList<>());
        }

        me.getFollowing().add(targetUserId);
        target.getFollowers().add(me.getId());

        repo.save(me);
        repo.save(target);
    }
    
    public void unfollowUser(String myEmail, String targetUserId) {

        AppUser me = repo.findByEmail(myEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AppUser target = repo.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        me.getFollowing().remove(targetUserId);
        target.getFollowers().remove(me.getId());

        repo.save(me);
        repo.save(target);
    }
}