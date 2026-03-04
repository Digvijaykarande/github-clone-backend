package gitHubClone.demo.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class CodeFile {

    private String id;
    private String path;     
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CodeFile() {}

    public CodeFile(String path, String content) {
        this.id = UUID.randomUUID().toString();
        this.path = path;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() { return id; }

    public String getPath() { return path; }

    public String getContent() { return content; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
}