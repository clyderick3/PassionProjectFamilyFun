package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Journaling;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface JournalingRepositoryWithBagRelationships {
    Optional<Journaling> fetchBagRelationships(Optional<Journaling> journaling);

    List<Journaling> fetchBagRelationships(List<Journaling> journalings);

    Page<Journaling> fetchBagRelationships(Page<Journaling> journalings);
}
