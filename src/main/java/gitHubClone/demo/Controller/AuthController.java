package gitHubClone.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gitHubClone.demo.security.AppUser;
import gitHubClone.demo.security.JwtResponse;
import gitHubClone.demo.security.LoginRequest;
import gitHubClone.demo.services.JwtService;
import gitHubClone.demo.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public String register(@RequestBody AppUser user) {
        userService.createUser(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		request.getEmail(), 
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken(request.getEmail());

        return new JwtResponse(token);
    }
}