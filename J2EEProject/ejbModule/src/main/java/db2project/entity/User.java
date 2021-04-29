package db2project.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "user", schema = "DB2Project")
@NamedQueries({
    @NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r WHERE r.username = ?1 and r.password = ?2"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT r FROM User r WHERE r.username = ?1"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT r FROM User r WHERE r.email = ?1"),
    @NamedQuery(name = "User.findByUsernameOrEmail", query = "SELECT r FROM User r WHERE r.username = ?1 or r.email = ?2")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Unique id generated by the persistence provider
    private int id;
    private String username;
    private String password;
    private String email;
    private byte isBlocked;
    private byte isAdmin;

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "user",
        // quando viene effettuata l'operazione X su di me (User), effettuala anche a questa relazione (Access)
        cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH },
        orphanRemoval = true //se viene tolto un accesso dalla lista, cancella quell'accesso
    )
    private List<Access> accesses;

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "user",
        // quando viene effettuata l'operazione X su di me (User), effettuala anche a questa relazione (Review)
        cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH },
        orphanRemoval = true //se viene tolta una review dalla lista, cancella quella review
    )
    private List<Review> reviews;

    public User() { }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isBlocked = (byte) 0;
        this.isAdmin = (byte) 0;
    }

    public List<Access> getAccesses() { return accesses; }

    public List<Review> getReviews() { return reviews; }

    public void addAccess(Access a) {
        if (accesses == null) {
            accesses = new LinkedList<>();
        }
        accesses.add(a);
    }



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isBlocked() { return isBlocked == ((byte) (1)); }
    public void setIsBlocked(boolean isBlocked) { this.isBlocked = isBlocked ? (byte) 1 : (byte) 0; }

    public boolean isAdmin() { return isAdmin == ((byte) (1)); }
    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin ? (byte) 1 : (byte) 0; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() { return id; }
}
