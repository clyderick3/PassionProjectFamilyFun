package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Journaling;
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
public class JournalingRepositoryWithBagRelationshipsImpl implements JournalingRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Journaling> fetchBagRelationships(Optional<Journaling> journaling) {
        return journaling.map(this::fetchFamilyTrees);
    }

    @Override
    public Page<Journaling> fetchBagRelationships(Page<Journaling> journalings) {
        return new PageImpl<>(fetchBagRelationships(journalings.getContent()), journalings.getPageable(), journalings.getTotalElements());
    }

    @Override
    public List<Journaling> fetchBagRelationships(List<Journaling> journalings) {
        return Optional.of(journalings).map(this::fetchFamilyTrees).orElse(Collections.emptyList());
    }

    Journaling fetchFamilyTrees(Journaling result) {
        return entityManager
            .createQuery(
                "select journaling from Journaling journaling left join fetch journaling.familyTrees where journaling is :journaling",
                Journaling.class
            )
            .setParameter("journaling", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Journaling> fetchFamilyTrees(List<Journaling> journalings) {
        return entityManager
            .createQuery(
                "select distinct journaling from Journaling journaling left join fetch journaling.familyTrees where journaling in :journalings",
                Journaling.class
            )
            .setParameter("journalings", journalings)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
