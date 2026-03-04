package gitHubClone.demo.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gitHubClone.demo.dto.AddFileRequest;
import gitHubClone.demo.dto.CreateRepoRequest;
import gitHubClone.demo.dto.UpdateFileRequest;
import gitHubClone.demo.model.CodeFile;
import gitHubClone.demo.model.Commit;
import gitHubClone.demo.model.Repo;
import gitHubClone.demo.services.RepoService;

@RestController
@RequestMapping("/api/repos")
public class RepoController {
	@Autowired
	private RepoService repoService;
	
	//create repo
	@PostMapping
	public Repo createRepo(@RequestBody CreateRepoRequest request,Principal principal) {
		return repoService.createRepo(principal.getName(), request);
	}
	
	//get all the repositories list
	@GetMapping("/me")
	public List<Repo> getMyrepo(Principal principal){
		return repoService.getMyRepos(principal.getName());
	}
	
	//get repo buy id 
	@GetMapping("/{repoId}")
	public Repo getRepo(@PathVariable String repoId) {
		return repoService.getRepoById(repoId);
	}
	
	//delete repo by id
	@DeleteMapping("/{repoId}")
	public String deleteRepo(@PathVariable String repoId,Principal principal) {
		repoService.deleteRepo(repoId,principal.getName());
		return "Repository Deleted Successfully";
	}
	
	// Get all public repositories
	@GetMapping("/public")
	public List<Repo> getPublicRepos() {
	    return repoService.getPublicRepos();
	}


	// Get repositories by username
	@GetMapping("/user/{username}")
	public List<Repo> getReposByUsername(@PathVariable String username) {
	    return repoService.getReposByUsername(username);
	}
	
	@GetMapping("/search")
	public List<Repo> searchRepos(@RequestParam String q) {
	    return repoService.searchPublicRepos(q);
	}
	
	@PostMapping("/{repoId}/files")
	public Repo addFile(@PathVariable String repoId,
	                    @RequestBody AddFileRequest request,
	                    Principal principal) {
	    return repoService.addFile(
	            repoId,
	            principal.getName(),
	            request);
	}
	
	
	//update the file inside repo
	@PutMapping("/{repoId}/files/{fileId}")
	public Repo updateFile(@PathVariable String repoId,@PathVariable String fileId,
							@RequestBody UpdateFileRequest request,Principal principal) {
		return repoService.updateFile(repoId, fileId,principal.getName(), request);
	}
	
	//delete file inside repo
	
	@DeleteMapping("{repoId}/files/{fileId}")
	public Repo deleteFile(@PathVariable String repoId,@PathVariable String fileId,Principal principal) {
		return repoService.deleteFile(repoId, fileId,principal.getName());
	}
	
	@GetMapping("/{repoId}/files/{fileId}")
	public CodeFile getFile(@PathVariable String repoId,
	                        @PathVariable String fileId,
	                        Principal principal) {

	    return repoService.getSingleFile(
	            repoId,
	            fileId,
	            principal.getName()
	    );
	}
	
	@GetMapping("/{repoId}/commits")
	public List<Commit> getCommitHistory(@PathVariable String repoId,
	                                     Principal principal) {

	    return repoService.getCommitHistory(
	            repoId,
	            principal.getName()
	    );
	}
	
}
