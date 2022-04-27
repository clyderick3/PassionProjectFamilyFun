package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FamilyTree;
import com.mycompany.myapp.repository.FamilyTreeRepository;
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
 * Integration tests for the {@link FamilyTreeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FamilyTreeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AGE = "AAAAAAAAAA";
    private static final String UPDATED_AGE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/family-trees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FamilyTreeRepository familyTreeRepository;

    @Mock
    private FamilyTreeRepository familyTreeRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamilyTreeMockMvc;

    private FamilyTree familyTree;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyTree createEntity(EntityManager em) {
        FamilyTree familyTree = new FamilyTree().name(DEFAULT_NAME).age(DEFAULT_AGE).location(DEFAULT_LOCATION);
        return familyTree;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyTree createUpdatedEntity(EntityManager em) {
        FamilyTree familyTree = new FamilyTree().name(UPDATED_NAME).age(UPDATED_AGE).location(UPDATED_LOCATION);
        return familyTree;
    }

    @BeforeEach
    public void initTest() {
        familyTree = createEntity(em);
    }

    @Test
    @Transactional
    void createFamilyTree() throws Exception {
        int databaseSizeBeforeCreate = familyTreeRepository.findAll().size();
        // Create the FamilyTree
        restFamilyTreeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyTree)))
            .andExpect(status().isCreated());

        // Validate the FamilyTree in the database
        List<FamilyTree> familyTreeList = familyTreeRepository.findAll();
        assertThat(familyTreeList).hasSize(databaseSizeBeforeCreate + 1);
        FamilyTree testFamilyTree = familyTreeList.get(familyTreeList.size() - 1);
        assertThat(testFamilyTree.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFamilyTree.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testFamilyTree.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void createFamilyTreeWithExistingId() throws Exception {
        // Create the FamilyTree with an existing ID
        familyTree.setId(1L);

        int databaseSizeBeforeCreate = familyTreeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilyTreeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyTree)))
            .andExpect(status().isBadRequest());

        // Validate the FamilyTree in the database
        List<FamilyTree> familyTreeList = familyTreeRepository.findAll();
        assertThat(familyTreeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFamilyTrees() throws Exception {
        // Initialize the database
        familyTreeRepository.saveAndFlush(familyTree);

        // Get all the familyTreeList
        restFamilyTreeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyTree.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFamilyTreesWithEagerRelationshipsIsEnabled() throws Exception {
        when(familyTreeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFamilyTreeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(familyTreeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFamilyTreesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(familyTreeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFamilyTreeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(familyTreeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFamilyTree() throws Exception {
        // Initialize the database
        familyTreeRepository.saveAndFlush(familyTree);

        // Get the familyTree
        restFamilyTreeMockMvc
            .perform(get(ENTITY_API_URL_ID, familyTree.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(familyTree.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    @Transactional
    void getNonExistingFamilyTree() throws Exception {
        // Get the familyTree
        restFamilyTreeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFamilyTree() throws Exception {
        // Initialize the database
        familyTreeRepository.saveAndFlush(familyTree);

        int databaseSizeBeforeUpdate = familyTreeRepository.findAll().size();

        // Update the familyTree
        FamilyTree updatedFamilyTree = familyTreeRepository.findById(familyTree.getId()).get();
        // Disconnect from session so that the updates on updatedFamilyTree are not directly saved in db
        em.detach(updatedFamilyTree);
        updatedFamilyTree.name(UPDATED_NAME).age(UPDATED_AGE).location(UPDATED_LOCATION);

        restFamilyTreeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFamilyTree.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFamilyTree))
            )
            .andExpect(status().isOk());

        // Validate the FamilyTree in the database
        List<FamilyTree> familyTreeList = familyTreeRepository.findAll();
        assertThat(familyTreeList).hasSize(databaseSizeBeforeUpdate);
        FamilyTree testFamilyTree = familyTreeList.get(familyTreeList.size() - 1);
        assertThat(testFamilyTree.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFamilyTree.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testFamilyTree.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void putNonExistingFamilyTree() throws Exception {
        int databaseSizeBeforeUpdate = familyTreeRepository.findAll().size();
        familyTree.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyTreeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familyTree.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familyTree))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyTree in the database
        List<FamilyTree> familyTreeList = familyTreeRepository.findAll();
        assertThat(familyTreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamilyTree() throws Exception {
        int databaseSizeBeforeUpdate = familyTreeRepository.findAll().size();
        familyTree.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyTreeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familyTree))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyTree in the database
        List<FamilyTree> familyTreeList = familyTreeRepository.findAll();
        assertThat(familyTreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamilyTree() throws Exception {
        int databaseSizeBeforeUpdate = familyTreeRepository.findAll().size();
        familyTree.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyTreeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyTree)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyTree in the database
        List<FamilyTree> familyTreeList = familyTreeRepository.findAll();
        assertThat(familyTreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamilyTreeWithPatch() throws Exception {
        // Initialize the database
        familyTreeRepository.saveAndFlush(familyTree);

        int databaseSizeBeforeUpdate = familyTreeRepository.findAll().size();

        // Update the familyTree using partial update
        FamilyTree partialUpdatedFamilyTree = new FamilyTree();
        partialUpdatedFamilyTree.setId(familyTree.getId());

        partialUpdatedFamilyTree.age(UPDATED_AGE).location(UPDATED_LOCATION);

        restFamilyTreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyTree.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilyTree))
            )
            .andExpect(status().isOk());

        // Validate the FamilyTree in the database
        List<FamilyTree> familyTreeList = familyTreeRepository.findAll();
        assertThat(familyTreeList).hasSize(databaseSizeBeforeUpdate);
        FamilyTree testFamilyTree = familyTreeList.get(familyTreeList.size() - 1);
        assertThat(testFamilyTree.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFamilyTree.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testFamilyTree.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void fullUpdateFamilyTreeWithPatch() throws Exception {
        // Initialize the database
        familyTreeRepository.saveAndFlush(familyTree);

        int databaseSizeBeforeUpdate = familyTreeRepository.findAll().size();

        // Update the familyTree using partial update
        FamilyTree partialUpdatedFamilyTree = new FamilyTree();
        partialUpdatedFamilyTree.setId(familyTree.getId());

        partialUpdatedFamilyTree.name(UPDATED_NAME).age(UPDATED_AGE).location(UPDATED_LOCATION);

        restFamilyTreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyTree.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilyTree))
            )
            .andExpect(status().isOk());

        // Validate the FamilyTree in the database
        List<FamilyTree> familyTreeList = familyTreeRepository.findAll();
        assertThat(familyTreeList).hasSize(databaseSizeBeforeUpdate);
        FamilyTree testFamilyTree = familyTreeList.get(familyTreeList.size() - 1);
        assertThat(testFamilyTree.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFamilyTree.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testFamilyTree.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void patchNonExistingFamilyTree() throws Exception {
        int databaseSizeBeforeUpdate = familyTreeRepository.findAll().size();
        familyTree.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyTreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, familyTree.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familyTree))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyTree in the database
        List<FamilyTree> familyTreeList = familyTreeRepository.findAll();
        assertThat(familyTreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamilyTree() throws Exception {
        int databaseSizeBeforeUpdate = familyTreeRepository.findAll().size();
        familyTree.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyTreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familyTree))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyTree in the database
        List<FamilyTree> familyTreeList = familyTreeRepository.findAll();
        assertThat(familyTreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamilyTree() throws Exception {
        int databaseSizeBeforeUpdate = familyTreeRepository.findAll().size();
        familyTree.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyTreeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(familyTree))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyTree in the database
        List<FamilyTree> familyTreeList = familyTreeRepository.findAll();
        assertThat(familyTreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamilyTree() throws Exception {
        // Initialize the database
        familyTreeRepository.saveAndFlush(familyTree);

        int databaseSizeBeforeDelete = familyTreeRepository.findAll().size();

        // Delete the familyTree
        restFamilyTreeMockMvc
            .perform(delete(ENTITY_API_URL_ID, familyTree.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FamilyTree> familyTreeList = familyTreeRepository.findAll();
        assertThat(familyTreeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
