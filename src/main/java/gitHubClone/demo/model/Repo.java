package gitHubClone.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "repositories")
@CompoundIndexes({
    
    @CompoundIndex(name = "owner_name_unique", def = "{'ownerId': 1, 'name': 1}", unique = true),
    @CompoundIndex(name = "owner_private", def = "{'ownerUserName': 1, 'isPrivate': 1}")
})
public class Repo {

    @Id
    private String id;

    private String name;
    private String description;
    private String ownerId;
    private String ownerUserName;

    // @IndexedDB quickly filter all public repos without a full scan
    @Indexed
    private boolean isPrivate;

    private LocalDateTime createdAt;

    private List<CodeFile> files    = new ArrayList<>();
    private List<Commit>   commits  = new ArrayList<>();

    public Repo() {}

    public Repo(String name, String description,
                String ownerId, String ownerUserName,
                boolean isPrivate) {
        this.name          = name;
        this.description   = description;
        this.ownerId       = ownerId;
        this.ownerUserName = ownerUserName;
        this.isPrivate     = isPrivate;
        this.createdAt     = LocalDateTime.now();
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    public String getId()                           { return id; }
    public void   setId(String id)                  { this.id = id; }

    public String getName()                         { return name; }
    public void   setName(String name)              { this.name = name; }

    public String getDescription()                  { return description; }
    public void   setDescription(String d)          { this.description = d; }

    public String getOwnerId()                      { return ownerId; }
    public void   setOwnerId(String ownerId)        { this.ownerId = ownerId; }

    public String getOwnerUserName()                { return ownerUserName; }
    public void   setOwnerUserName(String u)        { this.ownerUserName = u; }

    public boolean isPrivate()                      { return isPrivate; }
    public void    setPrivate(boolean isPrivate)    { this.isPrivate = isPrivate; }

    public LocalDateTime getCreatedAt()             { return createdAt; }
    public void          setCreatedAt(LocalDateTime t) { this.createdAt = t; }

    public List<CodeFile> getFiles()                { return files; }
    public void           setFiles(List<CodeFile> f){ this.files = f; }

    public List<Commit> getCommits()                { return commits; }
    public void         setCommits(List<Commit> c)  { this.commits = c; }  
}