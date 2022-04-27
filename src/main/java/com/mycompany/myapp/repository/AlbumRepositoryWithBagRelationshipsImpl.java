package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Album;
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
public class AlbumRepositoryWithBagRelationshipsImpl implements AlbumRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Album> fetchBagRelationships(Optional<Album> album) {
        return album.map(this::fetchFamilyTrees);
    }

    @Override
    public Page<Album> fetchBagRelationships(Page<Album> albums) {
        return new PageImpl<>(fetchBagRelationships(albums.getContent()), albums.getPageable(), albums.getTotalElements());
    }

    @Override
    public List<Album> fetchBagRelationships(List<Album> albums) {
        return Optional.of(albums).map(this::fetchFamilyTrees).orElse(Collections.emptyList());
    }

    Album fetchFamilyTrees(Album result) {
        return entityManager
            .createQuery("select album from Album album left join fetch album.familyTrees where album is :album", Album.class)
            .setParameter("album", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Album> fetchFamilyTrees(List<Album> albums) {
        return entityManager
            .createQuery("select distinct album from Album album left join fetch album.familyTrees where album in :albums", Album.class)
            .setParameter("albums", albums)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
