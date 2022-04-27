package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FamilyTree;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface FamilyTreeRepositoryWithBagRelationships {
    Optional<FamilyTree> fetchBagRelationships(Optional<FamilyTree> familyTree);

    List<FamilyTree> fetchBagRelationships(List<FamilyTree> familyTrees);

    Page<FamilyTree> fetchBagRelationships(Page<FamilyTree> familyTrees);
}
