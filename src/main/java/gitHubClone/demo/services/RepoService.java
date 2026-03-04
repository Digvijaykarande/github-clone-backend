package gitHubClone.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gitHubClone.demo.dto.AddFileRequest;
import gitHubClone.demo.dto.CreateRepoRequest;
import gitHubClone.demo.dto.UpdateFileRequest;
import gitHubClone.demo.exception.BadRequestException;
import gitHubClone.demo.exception.ForbiddenException;
import gitHubClone.demo.exception.NotFoundException;
import gitHubClone.demo.model.CodeFile;
import gitHubClone.demo.model.Commit;
import gitHubClone.demo.model.Repo;
import gitHubClone.demo.model.RepoRepository;
import gitHubClone.demo.repositories.UserRepository;
import gitHubClone.demo.security.AppUser;

@Service
public class RepoService {

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private UserRepository userRepository;

    // =========================
    // 🔹 Helper Methods
    // =========================

    private AppUser getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Repo getRepo(String repoId) {
        return repoRepository.findById(repoId)
                .orElseThrow(() -> new NotFoundException("Repository not found"));
    }

    private void checkOwner(Repo repo, AppUser user) {
        if (!repo.getOwnerId().equals(user.getId())) {
            throw new ForbiddenException("You are NOT allowed to modify this repo");
        }
    }

    // =========================
    // 🔹 Repo APIs
    // =========================

    public Repo createRepo(String email, CreateRepoRequest request) {

        AppUser user = getUser(email);

        boolean exists = repoRepository
                .findByOwnerIdAndName(user.getId(), request.getName())
                .isPresent();

        if (exists) {
            throw new BadRequestException("Repository with this name already exists");
        }

        Repo repo = new Repo(
                request.getName(),
                request.getDescription(),
                user.getId(),
                user.getUsername(),
                request.isPrivate()
        );

        return repoRepository.save(repo);
    }

    public List<Repo> getMyRepos(String email) {
        AppUser user = getUser(email);
        return repoRepository.findByOwnerId(user.getId());
    }

    public Repo getRepoById(String repoId) {
        return getRepo(repoId);
    }

    public Repo deleteRepo(String repoId, String email) {

        Repo repo = getRepo(repoId);
        AppUser user = getUser(email);

        checkOwner(repo, user);

        repoRepository.delete(repo);
        return repo;
    }
    
 // Get all public repos
    public List<Repo> getPublicRepos() {
        return repoRepository.findByIsPrivateFalse();
    }

    // Get public repos of specific user
    public List<Repo> getReposByUsername(String username) {
        return repoRepository.findByOwnerUserNameAndIsPrivateFalse(username);
    }
    
    public List<Repo> searchPublicRepos(String query) {

        if (query == null || query.isBlank()) {
            throw new BadRequestException("Search query cannot be empty");
        }

        return repoRepository
                .findByIsPrivateFalseAndNameContainingIgnoreCase(query);
    }
    
    // =========================
    // 🔹 File APIs
    // =========================

    public Repo addFile(String repoId, String email, AddFileRequest request) {

        Repo repo = getRepo(repoId);
        AppUser user = getUser(email);

        checkOwner(repo, user);

        boolean exists = repo.getFiles().stream()
                .anyMatch(f -> f.getPath().equals(request.getPath()));

        if (exists) {
            throw new BadRequestException("File with this name already exists");
        }

        CodeFile file = new CodeFile(
                request.getPath(),
                request.getContent()
        );
        
        repo.getFiles().add(file);
        createAutoCommit(repo, email, "Added file: " + request.getPath());

        return repoRepository.save(repo);
    }

    public Repo updateFile(String repoId,
                           String fileId,
                           String email,
                           UpdateFileRequest request) {

        Repo repo = getRepo(repoId);
        AppUser user = getUser(email);

        checkOwner(repo, user);

        CodeFile file = repo.getFiles().stream()
                .filter(f -> f.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("File not found"));

        file.setContent(request.getContent());
        createAutoCommit(repo, email, "Updated file: " + file.getPath());
        
        return repoRepository.save(repo);
    }

    public Repo deleteFile(String repoId,
                           String fileId,
                           String email) {

        Repo repo = getRepo(repoId);
        AppUser user = getUser(email);

        checkOwner(repo, user);

        boolean removed = repo.getFiles()
                .removeIf(f -> f.getId().equals(fileId));

        if (!removed) {
            throw new NotFoundException("File not found");
        }
        
        createAutoCommit(repo, email, "Deleted file");
        return repoRepository.save(repo);
    }

    public CodeFile getSingleFile(String repoId,
                                  String fileId,
                                  String email) {

        Repo repo = getRepo(repoId);

        if (repo.isPrivate()) {
            AppUser user = getUser(email);
            checkOwner(repo, user);
        }

        return repo.getFiles().stream()
                .filter(f -> f.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("File not found"));
    }
    
    private void createAutoCommit(Repo repo, String author, String message) {

        // Deep copy file list
        List<CodeFile> snapshot = repo.getFiles().stream()
                .map(file -> new CodeFile(
                        file.getPath(),
                        file.getContent()
                ))
                .toList();

        Commit commit = new Commit(message, author, snapshot);

        repo.getCommits().add(commit);
    }
    
    public List<Commit> getCommitHistory(String repoId, String email) {

        Repo repo = getRepo(repoId);

        if (repo.isPrivate()) {
            AppUser user = getUser(email);
            checkOwner(repo, user);
        }

        return repo.getCommits();
    }
}