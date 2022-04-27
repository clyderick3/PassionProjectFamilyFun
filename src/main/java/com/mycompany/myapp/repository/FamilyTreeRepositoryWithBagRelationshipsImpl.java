package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FamilyTree;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class FamilyTreeRepositoryWithBagRelationshipsImpl implements FamilyTreeRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<FamilyTree> fetchBagRelationships(Optional<FamilyTree> familyTree) {
        return familyTree.map(this::fetchUsers);
    }

    @Override
    public Page<FamilyTree> fetchBagRelationships(Page<FamilyTree> familyTrees) {
        return new PageImpl<>(fetchBagRelationships(familyTrees.getContent()), familyTrees.getPageable(), familyTrees.getTotalElements());
    }

    @Override
    public List<FamilyTree> fetchBagRelationships(List<FamilyTree> familyTrees) {
        return Optional.of(familyTrees).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    FamilyTree fetchUsers(FamilyTree result) {
        return entityManager
            .createQuery(
                "select familyTree from FamilyTree familyTree left join fetch familyTree.users where familyTree is :familyTree",
                FamilyTree.class
            )
            .setParameter("familyTree", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<FamilyTree> fetchUsers(List<FamilyTree> familyTrees) {
        return entityManager
            .createQuery(
                "select distinct familyTree from FamilyTree familyTree left join fetch familyTree.users where familyTree in :familyTrees",
                FamilyTree.class
            )
            .setParameter("familyTrees", familyTrees)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
