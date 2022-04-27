package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Journaling;
import com.mycompany.myapp.repository.JournalingRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JournalingResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JournalingResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final Long DEFAULT_JOURNAL_ENTRY = 1L;
    private static final Long UPDATED_JOURNAL_ENTRY = 2L;

    private static final String ENTITY_API_URL = "/api/journalings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JournalingRepository journalingRepository;

    @Mock
    private JournalingRepository journalingRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJournalingMockMvc;

    private Journaling journaling;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Journaling createEntity(EntityManager em) {
        Journaling journaling = new Journaling().title(DEFAULT_TITLE).date(DEFAULT_DATE).journalEntry(DEFAULT_JOURNAL_ENTRY);
        return journaling;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Journaling createUpdatedEntity(EntityManager em) {
        Journaling journaling = new Journaling().title(UPDATED_TITLE).date(UPDATED_DATE).journalEntry(UPDATED_JOURNAL_ENTRY);
        return journaling;
    }

    @BeforeEach
    public void initTest() {
        journaling = createEntity(em);
    }

    @Test
    @Transactional
    void createJournaling() throws Exception {
        int databaseSizeBeforeCreate = journalingRepository.findAll().size();
        // Create the Journaling
        restJournalingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(journaling)))
            .andExpect(status().isCreated());

        // Validate the Journaling in the database
        List<Journaling> journalingList = journalingRepository.findAll();
        assertThat(journalingList).hasSize(databaseSizeBeforeCreate + 1);
        Journaling testJournaling = journalingList.get(journalingList.size() - 1);
        assertThat(testJournaling.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJournaling.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testJournaling.getJournalEntry()).isEqualTo(DEFAULT_JOURNAL_ENTRY);
    }

    @Test
    @Transactional
    void createJournalingWithExistingId() throws Exception {
        // Create the Journaling with an existing ID
        journaling.setId(1L);

        int databaseSizeBeforeCreate = journalingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJournalingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(journaling)))
            .andExpect(status().isBadRequest());

        // Validate the Journaling in the database
        List<Journaling> journalingList = journalingRepository.findAll();
        assertThat(journalingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllJournalings() throws Exception {
        // Initialize the database
        journalingRepository.saveAndFlush(journaling);

        // Get all the journalingList
        restJournalingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journaling.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].journalEntry").value(hasItem(DEFAULT_JOURNAL_ENTRY.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJournalingsWithEagerRelationshipsIsEnabled() throws Exception {
        when(journalingRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJournalingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(journalingRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJournalingsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(journalingRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJournalingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(journalingRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getJournaling() throws Exception {
        // Initialize the database
        journalingRepository.saveAndFlush(journaling);

        // Get the journaling
        restJournalingMockMvc
            .perform(get(ENTITY_API_URL_ID, journaling.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(journaling.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
            .andExpect(jsonPath("$.journalEntry").value(DEFAULT_JOURNAL_ENTRY.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingJournaling() throws Exception {
        // Get the journaling
        restJournalingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJournaling() throws Exception {
        // Initialize the database
        journalingRepository.saveAndFlush(journaling);

        int databaseSizeBeforeUpdate = journalingRepository.findAll().size();

        // Update the journaling
        Journaling updatedJournaling = journalingRepository.findById(journaling.getId()).get();
        // Disconnect from session so that the updates on updatedJournaling are not directly saved in db
        em.detach(updatedJournaling);
        updatedJournaling.title(UPDATED_TITLE).date(UPDATED_DATE).journalEntry(UPDATED_JOURNAL_ENTRY);

        restJournalingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJournaling.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJournaling))
            )
            .andExpect(status().isOk());

        // Validate the Journaling in the database
        List<Journaling> journalingList = journalingRepository.findAll();
        assertThat(journalingList).hasSize(databaseSizeBeforeUpdate);
        Journaling testJournaling = journalingList.get(journalingList.size() - 1);
        assertThat(testJournaling.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testJournaling.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testJournaling.getJournalEntry()).isEqualTo(UPDATED_JOURNAL_ENTRY);
    }

    @Test
    @Transactional
    void putNonExistingJournaling() throws Exception {
        int databaseSizeBeforeUpdate = journalingRepository.findAll().size();
        journaling.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJournalingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, journaling.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(journaling))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journaling in the database
        List<Journaling> journalingList = journalingRepository.findAll();
        assertThat(journalingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJournaling() throws Exception {
        int databaseSizeBeforeUpdate = journalingRepository.findAll().size();
        journaling.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(journaling))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journaling in the database
        List<Journaling> journalingList = journalingRepository.findAll();
        assertThat(journalingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJournaling() throws Exception {
        int databaseSizeBeforeUpdate = journalingRepository.findAll().size();
        journaling.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(journaling)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Journaling in the database
        List<Journaling> journalingList = journalingRepository.findAll();
        assertThat(journalingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJournalingWithPatch() throws Exception {
        // Initialize the database
        journalingRepository.saveAndFlush(journaling);

        int databaseSizeBeforeUpdate = journalingRepository.findAll().size();

        // Update the journaling using partial update
        Journaling partialUpdatedJournaling = new Journaling();
        partialUpdatedJournaling.setId(journaling.getId());

        partialUpdatedJournaling.journalEntry(UPDATED_JOURNAL_ENTRY);

        restJournalingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJournaling.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJournaling))
            )
            .andExpect(status().isOk());

        // Validate the Journaling in the database
        List<Journaling> journalingList = journalingRepository.findAll();
        assertThat(journalingList).hasSize(databaseSizeBeforeUpdate);
        Journaling testJournaling = journalingList.get(journalingList.size() - 1);
        assertThat(testJournaling.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJournaling.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testJournaling.getJournalEntry()).isEqualTo(UPDATED_JOURNAL_ENTRY);
    }

    @Test
    @Transactional
    void fullUpdateJournalingWithPatch() throws Exception {
        // Initialize the database
        journalingRepository.saveAndFlush(journaling);

        int databaseSizeBeforeUpdate = journalingRepository.findAll().size();

        // Update the journaling using partial update
        Journaling partialUpdatedJournaling = new Journaling();
        partialUpdatedJournaling.setId(journaling.getId());

        partialUpdatedJournaling.title(UPDATED_TITLE).date(UPDATED_DATE).journalEntry(UPDATED_JOURNAL_ENTRY);

        restJournalingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJournaling.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJournaling))
            )
            .andExpect(status().isOk());

        // Validate the Journaling in the database
        List<Journaling> journalingList = journalingRepository.findAll();
        assertThat(journalingList).hasSize(databaseSizeBeforeUpdate);
        Journaling testJournaling = journalingList.get(journalingList.size() - 1);
        assertThat(testJournaling.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testJournaling.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testJournaling.getJournalEntry()).isEqualTo(UPDATED_JOURNAL_ENTRY);
    }

    @Test
    @Transactional
    void patchNonExistingJournaling() throws Exception {
        int databaseSizeBeforeUpdate = journalingRepository.findAll().size();
        journaling.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJournalingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, journaling.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(journaling))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journaling in the database
        List<Journaling> journalingList = journalingRepository.findAll();
        assertThat(journalingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJournaling() throws Exception {
        int databaseSizeBeforeUpdate = journalingRepository.findAll().size();
        journaling.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(journaling))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journaling in the database
        List<Journaling> journalingList = journalingRepository.findAll();
        assertThat(journalingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJournaling() throws Exception {
        int databaseSizeBeforeUpdate = journalingRepository.findAll().size();
        journaling.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(journaling))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Journaling in the database
        List<Journaling> journalingList = journalingRepository.findAll();
        assertThat(journalingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJournaling() throws Exception {
        // Initialize the database
        journalingRepository.saveAndFlush(journaling);

        int databaseSizeBeforeDelete = journalingRepository.findAll().size();

        // Delete the journaling
        restJournalingMockMvc
            .perform(delete(ENTITY_API_URL_ID, journaling.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Journaling> journalingList = journalingRepository.findAll();
        assertThat(journalingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
