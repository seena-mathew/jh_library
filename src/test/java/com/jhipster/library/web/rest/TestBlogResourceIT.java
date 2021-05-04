package com.jhipster.library.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jhipster.library.IntegrationTest;
import com.jhipster.library.domain.TestBlog;
import com.jhipster.library.repository.TestBlogRepository;
import com.jhipster.library.repository.search.TestBlogSearchRepository;
import com.jhipster.library.service.dto.TestBlogDTO;
import com.jhipster.library.service.mapper.TestBlogMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TestBlogResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TestBlogResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-blogs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/test-blogs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestBlogRepository testBlogRepository;

    @Autowired
    private TestBlogMapper testBlogMapper;

    /**
     * This repository is mocked in the com.jhipster.library.repository.search test package.
     *
     * @see com.jhipster.library.repository.search.TestBlogSearchRepositoryMockConfiguration
     */
    @Autowired
    private TestBlogSearchRepository mockTestBlogSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestBlogMockMvc;

    private TestBlog testBlog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestBlog createEntity(EntityManager em) {
        TestBlog testBlog = new TestBlog()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON)
            .username(DEFAULT_USERNAME);
        return testBlog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestBlog createUpdatedEntity(EntityManager em) {
        TestBlog testBlog = new TestBlog()
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .username(UPDATED_USERNAME);
        return testBlog;
    }

    @BeforeEach
    public void initTest() {
        testBlog = createEntity(em);
    }

    @Test
    @Transactional
    void createTestBlog() throws Exception {
        int databaseSizeBeforeCreate = testBlogRepository.findAll().size();
        // Create the TestBlog
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(testBlog);
        restTestBlogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testBlogDTO)))
            .andExpect(status().isCreated());

        // Validate the TestBlog in the database
        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeCreate + 1);
        TestBlog testTestBlog = testBlogList.get(testBlogList.size() - 1);
        assertThat(testTestBlog.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTestBlog.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testTestBlog.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testTestBlog.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testTestBlog.getUsername()).isEqualTo(DEFAULT_USERNAME);

        // Validate the TestBlog in Elasticsearch
        verify(mockTestBlogSearchRepository, times(1)).save(testTestBlog);
    }

    @Test
    @Transactional
    void createTestBlogWithExistingId() throws Exception {
        // Create the TestBlog with an existing ID
        testBlog.setId(1L);
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(testBlog);

        int databaseSizeBeforeCreate = testBlogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestBlogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testBlogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TestBlog in the database
        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeCreate);

        // Validate the TestBlog in Elasticsearch
        verify(mockTestBlogSearchRepository, times(0)).save(testBlog);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = testBlogRepository.findAll().size();
        // set the field null
        testBlog.setTitle(null);

        // Create the TestBlog, which fails.
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(testBlog);

        restTestBlogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testBlogDTO)))
            .andExpect(status().isBadRequest());

        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = testBlogRepository.findAll().size();
        // set the field null
        testBlog.setContent(null);

        // Create the TestBlog, which fails.
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(testBlog);

        restTestBlogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testBlogDTO)))
            .andExpect(status().isBadRequest());

        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = testBlogRepository.findAll().size();
        // set the field null
        testBlog.setCreatedOn(null);

        // Create the TestBlog, which fails.
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(testBlog);

        restTestBlogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testBlogDTO)))
            .andExpect(status().isBadRequest());

        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = testBlogRepository.findAll().size();
        // set the field null
        testBlog.setUpdatedOn(null);

        // Create the TestBlog, which fails.
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(testBlog);

        restTestBlogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testBlogDTO)))
            .andExpect(status().isBadRequest());

        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = testBlogRepository.findAll().size();
        // set the field null
        testBlog.setUsername(null);

        // Create the TestBlog, which fails.
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(testBlog);

        restTestBlogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testBlogDTO)))
            .andExpect(status().isBadRequest());

        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTestBlogs() throws Exception {
        // Initialize the database
        testBlogRepository.saveAndFlush(testBlog);

        // Get all the testBlogList
        restTestBlogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testBlog.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)));
    }

    @Test
    @Transactional
    void getTestBlog() throws Exception {
        // Initialize the database
        testBlogRepository.saveAndFlush(testBlog);

        // Get the testBlog
        restTestBlogMockMvc
            .perform(get(ENTITY_API_URL_ID, testBlog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testBlog.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME));
    }

    @Test
    @Transactional
    void getNonExistingTestBlog() throws Exception {
        // Get the testBlog
        restTestBlogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTestBlog() throws Exception {
        // Initialize the database
        testBlogRepository.saveAndFlush(testBlog);

        int databaseSizeBeforeUpdate = testBlogRepository.findAll().size();

        // Update the testBlog
        TestBlog updatedTestBlog = testBlogRepository.findById(testBlog.getId()).get();
        // Disconnect from session so that the updates on updatedTestBlog are not directly saved in db
        em.detach(updatedTestBlog);
        updatedTestBlog
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .username(UPDATED_USERNAME);
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(updatedTestBlog);

        restTestBlogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testBlogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testBlogDTO))
            )
            .andExpect(status().isOk());

        // Validate the TestBlog in the database
        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeUpdate);
        TestBlog testTestBlog = testBlogList.get(testBlogList.size() - 1);
        assertThat(testTestBlog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestBlog.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testTestBlog.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTestBlog.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testTestBlog.getUsername()).isEqualTo(UPDATED_USERNAME);

        // Validate the TestBlog in Elasticsearch
        verify(mockTestBlogSearchRepository).save(testTestBlog);
    }

    @Test
    @Transactional
    void putNonExistingTestBlog() throws Exception {
        int databaseSizeBeforeUpdate = testBlogRepository.findAll().size();
        testBlog.setId(count.incrementAndGet());

        // Create the TestBlog
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(testBlog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestBlogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testBlogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testBlogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestBlog in the database
        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TestBlog in Elasticsearch
        verify(mockTestBlogSearchRepository, times(0)).save(testBlog);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestBlog() throws Exception {
        int databaseSizeBeforeUpdate = testBlogRepository.findAll().size();
        testBlog.setId(count.incrementAndGet());

        // Create the TestBlog
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(testBlog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestBlogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testBlogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestBlog in the database
        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TestBlog in Elasticsearch
        verify(mockTestBlogSearchRepository, times(0)).save(testBlog);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestBlog() throws Exception {
        int databaseSizeBeforeUpdate = testBlogRepository.findAll().size();
        testBlog.setId(count.incrementAndGet());

        // Create the TestBlog
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(testBlog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestBlogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testBlogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestBlog in the database
        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TestBlog in Elasticsearch
        verify(mockTestBlogSearchRepository, times(0)).save(testBlog);
    }

    @Test
    @Transactional
    void partialUpdateTestBlogWithPatch() throws Exception {
        // Initialize the database
        testBlogRepository.saveAndFlush(testBlog);

        int databaseSizeBeforeUpdate = testBlogRepository.findAll().size();

        // Update the testBlog using partial update
        TestBlog partialUpdatedTestBlog = new TestBlog();
        partialUpdatedTestBlog.setId(testBlog.getId());

        partialUpdatedTestBlog.createdOn(UPDATED_CREATED_ON).username(UPDATED_USERNAME);

        restTestBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestBlog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestBlog))
            )
            .andExpect(status().isOk());

        // Validate the TestBlog in the database
        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeUpdate);
        TestBlog testTestBlog = testBlogList.get(testBlogList.size() - 1);
        assertThat(testTestBlog.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTestBlog.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testTestBlog.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTestBlog.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testTestBlog.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void fullUpdateTestBlogWithPatch() throws Exception {
        // Initialize the database
        testBlogRepository.saveAndFlush(testBlog);

        int databaseSizeBeforeUpdate = testBlogRepository.findAll().size();

        // Update the testBlog using partial update
        TestBlog partialUpdatedTestBlog = new TestBlog();
        partialUpdatedTestBlog.setId(testBlog.getId());

        partialUpdatedTestBlog
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .username(UPDATED_USERNAME);

        restTestBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestBlog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestBlog))
            )
            .andExpect(status().isOk());

        // Validate the TestBlog in the database
        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeUpdate);
        TestBlog testTestBlog = testBlogList.get(testBlogList.size() - 1);
        assertThat(testTestBlog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestBlog.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testTestBlog.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTestBlog.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testTestBlog.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void patchNonExistingTestBlog() throws Exception {
        int databaseSizeBeforeUpdate = testBlogRepository.findAll().size();
        testBlog.setId(count.incrementAndGet());

        // Create the TestBlog
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(testBlog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testBlogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testBlogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestBlog in the database
        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TestBlog in Elasticsearch
        verify(mockTestBlogSearchRepository, times(0)).save(testBlog);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestBlog() throws Exception {
        int databaseSizeBeforeUpdate = testBlogRepository.findAll().size();
        testBlog.setId(count.incrementAndGet());

        // Create the TestBlog
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(testBlog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testBlogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestBlog in the database
        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TestBlog in Elasticsearch
        verify(mockTestBlogSearchRepository, times(0)).save(testBlog);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestBlog() throws Exception {
        int databaseSizeBeforeUpdate = testBlogRepository.findAll().size();
        testBlog.setId(count.incrementAndGet());

        // Create the TestBlog
        TestBlogDTO testBlogDTO = testBlogMapper.toDto(testBlog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestBlogMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testBlogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestBlog in the database
        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TestBlog in Elasticsearch
        verify(mockTestBlogSearchRepository, times(0)).save(testBlog);
    }

    @Test
    @Transactional
    void deleteTestBlog() throws Exception {
        // Initialize the database
        testBlogRepository.saveAndFlush(testBlog);

        int databaseSizeBeforeDelete = testBlogRepository.findAll().size();

        // Delete the testBlog
        restTestBlogMockMvc
            .perform(delete(ENTITY_API_URL_ID, testBlog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestBlog> testBlogList = testBlogRepository.findAll();
        assertThat(testBlogList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TestBlog in Elasticsearch
        verify(mockTestBlogSearchRepository, times(1)).deleteById(testBlog.getId());
    }

    @Test
    @Transactional
    void searchTestBlog() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        testBlogRepository.saveAndFlush(testBlog);
        when(mockTestBlogSearchRepository.search(queryStringQuery("id:" + testBlog.getId())))
            .thenReturn(Collections.singletonList(testBlog));

        // Search the testBlog
        restTestBlogMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + testBlog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testBlog.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)));
    }
}
