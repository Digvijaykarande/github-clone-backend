package gitHubClone.demo.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Commit {

    private String id;
    private String message;
    private String author;
    private LocalDateTime createdAt;
    private List<CodeFile> snapshot;

    public Commit() {}

    public Commit(String message, String author, List<CodeFile> snapshot) {
        this.id = UUID.randomUUID().toString();
        this.message = message;
        this.author = author;
        this.createdAt = LocalDateTime.now();
        this.snapshot = snapshot;
    }

    public String getId() { return id; }
    public String getMessage() { return message; }
    public String getAuthor() { return author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<CodeFile> getSnapshot() { return snapshot; }
}