package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Photo.
 */
@Entity
@Table(name = "photo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private String date;

    @Column(name = "height")
    private Integer height;

    @Column(name = "width")
    private Integer width;

    @OneToMany(mappedBy = "photo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "users", "photo", "journalings", "albums" }, allowSetters = true)
    private Set<FamilyTree> familyTrees = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "familyTrees", "photos" }, allowSetters = true)
    private Album album;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Photo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Photo title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Photo description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return this.date;
    }

    public Photo date(String date) {
        this.setDate(date);
        return this;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Photo height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Photo width(Integer width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Set<FamilyTree> getFamilyTrees() {
        return this.familyTrees;
    }

    public void setFamilyTrees(Set<FamilyTree> familyTrees) {
        if (this.familyTrees != null) {
            this.familyTrees.forEach(i -> i.setPhoto(null));
        }
        if (familyTrees != null) {
            familyTrees.forEach(i -> i.setPhoto(this));
        }
        this.familyTrees = familyTrees;
    }

    public Photo familyTrees(Set<FamilyTree> familyTrees) {
        this.setFamilyTrees(familyTrees);
        return this;
    }

    public Photo addFamilyTree(FamilyTree familyTree) {
        this.familyTrees.add(familyTree);
        familyTree.setPhoto(this);
        return this;
    }

    public Photo removeFamilyTree(FamilyTree familyTree) {
        this.familyTrees.remove(familyTree);
        familyTree.setPhoto(null);
        return this;
    }

    public Album getAlbum() {
        return this.album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Photo album(Album album) {
        this.setAlbum(album);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Photo)) {
            return false;
        }
        return id != null && id.equals(((Photo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Photo{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            ", height=" + getHeight() +
            ", width=" + getWidth() +
            "}";
    }
}
