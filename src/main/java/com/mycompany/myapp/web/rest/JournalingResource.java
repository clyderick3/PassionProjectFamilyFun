package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Journaling;
import com.mycompany.myapp.repository.JournalingRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Journaling}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class JournalingResource {

    private final Logger log = LoggerFactory.getLogger(JournalingResource.class);

    private static final String ENTITY_NAME = "journaling";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JournalingRepository journalingRepository;

    public JournalingResource(JournalingRepository journalingRepository) {
        this.journalingRepository = journalingRepository;
    }

    /**
     * {@code POST  /journalings} : Create a new journaling.
     *
     * @param journaling the journaling to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new journaling, or with status {@code 400 (Bad Request)} if the journaling has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/journalings")
    public ResponseEntity<Journaling> createJournaling(@RequestBody Journaling journaling) throws URISyntaxException {
        log.debug("REST request to save Journaling : {}", journaling);
        if (journaling.getId() != null) {
            throw new BadRequestAlertException("A new journaling cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Journaling result = journalingRepository.save(journaling);
        return ResponseEntity
            .created(new URI("/api/journalings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /journalings/:id} : Updates an existing journaling.
     *
     * @param id the id of the journaling to save.
     * @param journaling the journaling to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated journaling,
     * or with status {@code 400 (Bad Request)} if the journaling is not valid,
     * or with status {@code 500 (Internal Server Error)} if the journaling couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/journalings/{id}")
    public ResponseEntity<Journaling> updateJournaling(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Journaling journaling
    ) throws URISyntaxException {
        log.debug("REST request to update Journaling : {}, {}", id, journaling);
        if (journaling.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, journaling.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!journalingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Journaling result = journalingRepository.save(journaling);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, journaling.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /journalings/:id} : Partial updates given fields of an existing journaling, field will ignore if it is null
     *
     * @param id the id of the journaling to save.
     * @param journaling the journaling to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated journaling,
     * or with status {@code 400 (Bad Request)} if the journaling is not valid,
     * or with status {@code 404 (Not Found)} if the journaling is not found,
     * or with status {@code 500 (Internal Server Error)} if the journaling couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/journalings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Journaling> partialUpdateJournaling(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Journaling journaling
    ) throws URISyntaxException {
        log.debug("REST request to partial update Journaling partially : {}, {}", id, journaling);
        if (journaling.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, journaling.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!journalingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Journaling> result = journalingRepository
            .findById(journaling.getId())
            .map(existingJournaling -> {
                if (journaling.getTitle() != null) {
                    existingJournaling.setTitle(journaling.getTitle());
                }
                if (journaling.getDate() != null) {
                    existingJournaling.setDate(journaling.getDate());
                }
                if (journaling.getJournalEntry() != null) {
                    existingJournaling.setJournalEntry(journaling.getJournalEntry());
                }

                return existingJournaling;
            })
            .map(journalingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, journaling.getId().toString())
        );
    }

    /**
     * {@code GET  /journalings} : get all the journalings.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of journalings in body.
     */
    @GetMapping("/journalings")
    public List<Journaling> getAllJournalings(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Journalings");
        return journalingRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /journalings/:id} : get the "id" journaling.
     *
     * @param id the id of the journaling to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the journaling, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/journalings/{id}")
    public ResponseEntity<Journaling> getJournaling(@PathVariable Long id) {
        log.debug("REST request to get Journaling : {}", id);
        Optional<Journaling> journaling = journalingRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(journaling);
    }

    /**
     * {@code DELETE  /journalings/:id} : delete the "id" journaling.
     *
     * @param id the id of the journaling to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/journalings/{id}")
    public ResponseEntity<Void> deleteJournaling(@PathVariable Long id) {
        log.debug("REST request to delete Journaling : {}", id);
        journalingRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
