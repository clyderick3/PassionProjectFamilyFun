package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FamilyTree;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FamilyTree entity.
 */
@Repository
public interface FamilyTreeRepository extends FamilyTreeRepositoryWithBagRelationships, JpaRepository<FamilyTree, Long> {
    default Optional<FamilyTree> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<FamilyTree> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<FamilyTree> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
