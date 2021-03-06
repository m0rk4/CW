package by.mark.mangareviewer.domain;

import by.mark.mangareviewer.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@ToString(of = {"id", "title"})
@EqualsAndHashCode(of = {"id", "title"})
@Indexed
@JsonIdentityInfo(
        property = "id",
        generator = ObjectIdGenerators.PropertyGenerator.class
)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;
    @JsonView(Views.IdText.class)
    @Field
    private String title;

    @Column(updatable = false)
    @JsonView(Views.IdText.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;

    @PrePersist
    void setCreationTime() {
        this.creationTime = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    @JsonView(Views.FullItem.class)
    @IndexedEmbedded
    private Collection collection;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH,
    })
    @JoinTable(
            name = "item_tag",
            joinColumns = {@JoinColumn(name = "item_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    @JsonView(Views.IdText.class)
    @IndexedEmbedded
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinTable(
            name = "item_liker",
            joinColumns = {@JoinColumn(name = "item_id")},
            inverseJoinColumns = {@JoinColumn(name = "liker_id")}
    )
    @JsonView({Views.FullItem.class, Views.FullCollection.class})
    private Set<User> likers = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item", orphanRemoval = true)
    @JsonView({Views.FullItem.class, Views.FullCollection.class})
    @IndexedEmbedded
    private Set<Value> values = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item", orphanRemoval = true)
    @JsonView({Views.FullItem.class, Views.FullCollection.class})
    @IndexedEmbedded
    private List<Comment> comments = new LinkedList<>();
}
