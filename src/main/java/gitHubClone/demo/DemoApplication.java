package gitHubClone.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}


//================= AUTH =================

//http://localhost:8081/auth/register    : POST
//http://localhost:8081/auth/login       : POST

//================= PROFILE =================

//http://localhost:8081/api/users/me      : GET
//http://localhost:8081/api/users/update  : PUT
//api/users/profile-image				 : POST 

//================= (Follow/Following APIs) =================

//POST 		/api/users/{userId}/follow
//DELETE 	/api/users/{userId}/follow
//GET 		/api/users/{userId}/following

//================= (Repository APIs) =================

//POST   http://localhost:8081/api/repos
//GET    http://localhost:8081/api/repos/me
//GET    http://localhost:8081/api/repos/{repoId+}
//DELETE http://localhost:8081/api/repos/{repoId}
//GET 	 http://localhost:8081/api/repos/public
//GET 	 http://localhost:8081/api/repos/user/digvijay
//GET 	 http://localhost:8081/api/repos/search?q=spring
//GET 	 http://localhost:8081/api/repos/{repoId}/commits


//================= (File APIs ) =================

//POST   http://localhost:8081/api/repos/{repoId}/files
//PUT	 http://localhost:8081/api/repos/{repoId}/files/{fileId}
//DELETE http://localhost:8081/api/repos/{repoId}/files/{fileId}
//GET http://localhost:8081/api/repos/{repoId}/files/{fileId}


//================= (Searching user ) =================

//GET /api/users/search?q=UserName