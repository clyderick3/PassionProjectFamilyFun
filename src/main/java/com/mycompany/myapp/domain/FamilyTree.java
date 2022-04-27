package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FamilyTree.
 */
@Entity
@Table(name = "family_tree")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FamilyTree implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private String age;

    @Column(name = "location")
    private String location;

    @ManyToMany
    @JoinTable(
        name = "rel_family_tree__user",
        joinColumns = @JoinColumn(name = "family_tree_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "familyTrees", "album" }, allowSetters = true)
    private Photo photo;

    @ManyToMany(mappedBy = "familyTrees")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "familyTrees" }, allowSetters = true)
    private Set<Journaling> journalings = new HashSet<>();

    @ManyToMany(mappedBy = "familyTrees")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "familyTrees", "photos" }, allowSetters = true)
    private Set<Album> albums = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FamilyTree id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public FamilyTree name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return this.age;
    }

    public FamilyTree age(String age) {
        this.setAge(age);
        return this;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation() {
        return this.location;
    }

    public FamilyTree location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public FamilyTree users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public FamilyTree addUser(User user) {
        this.users.add(user);
        return this;
    }

    public FamilyTree removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public Photo getPhoto() {
        return this.photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public FamilyTree photo(Photo photo) {
        this.setPhoto(photo);
        return this;
    }

    public Set<Journaling> getJournalings() {
        return this.journalings;
    }

    public void setJournalings(Set<Journaling> journalings) {
        if (this.journalings != null) {
            this.journalings.forEach(i -> i.removeFamilyTree(this));
        }
        if (journalings != null) {
            journalings.forEach(i -> i.addFamilyTree(this));
        }
        this.journalings = journalings;
    }

    public FamilyTree journalings(Set<Journaling> journalings) {
        this.setJournalings(journalings);
        return this;
    }

    public FamilyTree addJournaling(Journaling journaling) {
        this.journalings.add(journaling);
        journaling.getFamilyTrees().add(this);
        return this;
    }

    public FamilyTree removeJournaling(Journaling journaling) {
        this.journalings.remove(journaling);
        journaling.getFamilyTrees().remove(this);
        return this;
    }

    public Set<Album> getAlbums() {
        return this.albums;
    }

    public void setAlbums(Set<Album> albums) {
        if (this.albums != null) {
            this.albums.forEach(i -> i.removeFamilyTree(this));
        }
        if (albums != null) {
            albums.forEach(i -> i.addFamilyTree(this));
        }
        this.albums = albums;
    }

    public FamilyTree albums(Set<Album> albums) {
        this.setAlbums(albums);
        return this;
    }

    public FamilyTree addAlbum(Album album) {
        this.albums.add(album);
        album.getFamilyTrees().add(this);
        return this;
    }

    public FamilyTree removeAlbum(Album album) {
        this.albums.remove(album);
        album.getFamilyTrees().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FamilyTree)) {
            return false;
        }
        return id != null && id.equals(((FamilyTree) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FamilyTree{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age='" + getAge() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
