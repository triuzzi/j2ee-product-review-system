package db2project.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product", schema = "DB2Project")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Unique id generated by the persistence provider
    private int id;

    private String name;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private byte[] image;


    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "product",
        // quando viene effettuata l'operazione X su di me (Product), effettuala anche a questa relazione (MQuestion)
        cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH },
        orphanRemoval = true //se viene tolta una question dalla lista, cancella quella question
    )
    private List<MQuestion> questions;

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "product",
        // quando viene effettuata l'operazione X su di me (Product), effettuala anche a questa relazione (Review)
        cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH },
        orphanRemoval = true //se viene tolta una review dalla lista, cancella quella review
    )
    private List<Review> reviews;

    public Product() {}

    public Product(String name, Date date, byte[] image) {
        this.name = name;
        this.date = date;
        this.image = image;
    }

    public List<MQuestion> getQuestions() { return questions; }

    public List<Review> getReviews() { return reviews; }

    public void addQuestion(MQuestion q) { questions.add(q); }

    public void addReview(Review r) { reviews.add(r); }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() { return id; }
}
