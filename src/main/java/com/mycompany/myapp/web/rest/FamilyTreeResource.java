package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.FamilyTree;
import com.mycompany.myapp.repository.FamilyTreeRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.FamilyTree}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FamilyTreeResource {

    private final Logger log = LoggerFactory.getLogger(FamilyTreeResource.class);

    private static final String ENTITY_NAME = "familyTree";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamilyTreeRepository familyTreeRepository;

    public FamilyTreeResource(FamilyTreeRepository familyTreeRepository) {
        this.familyTreeRepository = familyTreeRepository;
    }

    /**
     * {@code POST  /family-trees} : Create a new familyTree.
     *
     * @param familyTree the familyTree to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new familyTree, or with status {@code 400 (Bad Request)} if the familyTree has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/family-trees")
    public ResponseEntity<FamilyTree> createFamilyTree(@RequestBody FamilyTree familyTree) throws URISyntaxException {
        log.debug("REST request to save FamilyTree : {}", familyTree);
        if (familyTree.getId() != null) {
            throw new BadRequestAlertException("A new familyTree cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FamilyTree result = familyTreeRepository.save(familyTree);
        return ResponseEntity
            .created(new URI("/api/family-trees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /family-trees/:id} : Updates an existing familyTree.
     *
     * @param id the id of the familyTree to save.
     * @param familyTree the familyTree to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyTree,
     * or with status {@code 400 (Bad Request)} if the familyTree is not valid,
     * or with status {@code 500 (Internal Server Error)} if the familyTree couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/family-trees/{id}")
    public ResponseEntity<FamilyTree> updateFamilyTree(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FamilyTree familyTree
    ) throws URISyntaxException {
        log.debug("REST request to update FamilyTree : {}, {}", id, familyTree);
        if (familyTree.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyTree.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyTreeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FamilyTree result = familyTreeRepository.save(familyTree);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyTree.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /family-trees/:id} : Partial updates given fields of an existing familyTree, field will ignore if it is null
     *
     * @param id the id of the familyTree to save.
     * @param familyTree the familyTree to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyTree,
     * or with status {@code 400 (Bad Request)} if the familyTree is not valid,
     * or with status {@code 404 (Not Found)} if the familyTree is not found,
     * or with status {@code 500 (Internal Server Error)} if the familyTree couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/family-trees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FamilyTree> partialUpdateFamilyTree(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FamilyTree familyTree
    ) throws URISyntaxException {
        log.debug("REST request to partial update FamilyTree partially : {}, {}", id, familyTree);
        if (familyTree.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyTree.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyTreeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FamilyTree> result = familyTreeRepository
            .findById(familyTree.getId())
            .map(existingFamilyTree -> {
                if (familyTree.getName() != null) {
                    existingFamilyTree.setName(familyTree.getName());
                }
                if (familyTree.getAge() != null) {
                    existingFamilyTree.setAge(familyTree.getAge());
                }
                if (familyTree.getLocation() != null) {
                    existingFamilyTree.setLocation(familyTree.getLocation());
                }

                return existingFamilyTree;
            })
            .map(familyTreeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyTree.getId().toString())
        );
    }

    /**
     * {@code GET  /family-trees} : get all the familyTrees.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of familyTrees in body.
     */
    @GetMapping("/family-trees")
    public List<FamilyTree> getAllFamilyTrees(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all FamilyTrees");
        return familyTreeRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /family-trees/:id} : get the "id" familyTree.
     *
     * @param id the id of the familyTree to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the familyTree, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/family-trees/{id}")
    public ResponseEntity<FamilyTree> getFamilyTree(@PathVariable Long id) {
        log.debug("REST request to get FamilyTree : {}", id);
        Optional<FamilyTree> familyTree = familyTreeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(familyTree);
    }

    /**
     * {@code DELETE  /family-trees/:id} : delete the "id" familyTree.
     *
     * @param id the id of the familyTree to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/family-trees/{id}")
    public ResponseEntity<Void> deleteFamilyTree(@PathVariable Long id) {
        log.debug("REST request to delete FamilyTree : {}", id);
        familyTreeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
