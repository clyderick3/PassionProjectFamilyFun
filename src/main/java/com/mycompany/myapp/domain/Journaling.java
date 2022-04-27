package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Journaling.
 */
@Entity
@Table(name = "journaling")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Journaling implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "date")
    private String date;

    @Column(name = "journal_entry")
    private Long journalEntry;

    @ManyToMany
    @JoinTable(
        name = "rel_journaling__family_tree",
        joinColumns = @JoinColumn(name = "journaling_id"),
        inverseJoinColumns = @JoinColumn(name = "family_tree_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "users", "photo", "journalings", "albums" }, allowSetters = true)
    private Set<FamilyTree> familyTrees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Journaling id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Journaling title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return this.date;
    }

    public Journaling date(String date) {
        this.setDate(date);
        return this;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getJournalEntry() {
        return this.journalEntry;
    }

    public Journaling journalEntry(Long journalEntry) {
        this.setJournalEntry(journalEntry);
        return this;
    }

    public void setJournalEntry(Long journalEntry) {
        this.journalEntry = journalEntry;
    }

    public Set<FamilyTree> getFamilyTrees() {
        return this.familyTrees;
    }

    public void setFamilyTrees(Set<FamilyTree> familyTrees) {
        this.familyTrees = familyTrees;
    }

    public Journaling familyTrees(Set<FamilyTree> familyTrees) {
        this.setFamilyTrees(familyTrees);
        return this;
    }

    public Journaling addFamilyTree(FamilyTree familyTree) {
        this.familyTrees.add(familyTree);
        familyTree.getJournalings().add(this);
        return this;
    }

    public Journaling removeFamilyTree(FamilyTree familyTree) {
        this.familyTrees.remove(familyTree);
        familyTree.getJournalings().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Journaling)) {
            return false;
        }
        return id != null && id.equals(((Journaling) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Journaling{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", date='" + getDate() + "'" +
            ", journalEntry=" + getJournalEntry() +
            "}";
    }
}
