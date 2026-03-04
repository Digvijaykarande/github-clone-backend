package gitHubClone.demo.Controller;

import java.security.Principal;

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
import gitHubClone.demo.services.UserService;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ProfileResponse getMyProfile(Authentication auth) {

        String email = auth.getName();
        return userService.getMyProfile(email);
    }
    
    
    @PostMapping("/profile-image")
    public String uploadImage(@RequestParam MultipartFile file,
                              Principal principal) throws IOException {

        return userService.uploadProfileImage(file, principal.getName());
    }
    
    @PutMapping("/update")
    public ProfileResponse updateProfile(
            Authentication auth,
            @RequestBody UpdateProfileRequest req) {

        return userService.updateProfile(auth.getName(), req);
    }
    
    @DeleteMapping("/{userId}/follow")
    public String unfollowUser(@PathVariable String userId, Principal principal) {

        userService.unfollowUser(principal.getName(), userId);
        return "Unfollowed successfully";
    }
    
    @PostMapping("/{userId}/follow")
    public String followUser(@PathVariable String userId, Principal principal) {

        userService.followUser(principal.getName(), userId);
        return "Followed successfully";
    }
}