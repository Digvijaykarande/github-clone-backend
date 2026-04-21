package gitHubClone.demo.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

	@RequestMapping("/")
	public String greet() {
		return "helow ";
	}

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("ok");
    }
}