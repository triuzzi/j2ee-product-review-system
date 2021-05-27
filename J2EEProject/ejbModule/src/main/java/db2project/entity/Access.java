package db2project.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "access", schema = "DB2Project")
public class Access implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Unique id generated by the persistence provider
    private int id;

    @ManyToOne // (Bi-directional) many-to-one association to User. I am the OWNER.
    @JoinColumn(name = "user") //nome della colonna che ha la FK a User
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date at;


    public Access() {}

    public Access(User user) { //updates both sides of the relationship
        this.user = user;
        this.at = new Date();
        user.addAccess(this);
    }

    public int getId() { return id; }
    //public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    //public void setUser(User user) { this.user = user; }

    public Date getAt() { return at; }
    //public void setAt(Date at) { this.at = at; }


}
