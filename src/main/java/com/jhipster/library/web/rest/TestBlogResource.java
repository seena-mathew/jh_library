package com.jhipster.library.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.jhipster.library.repository.TestBlogRepository;
import com.jhipster.library.service.TestBlogService;
import com.jhipster.library.service.dto.TestBlogDTO;
import com.jhipster.library.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jhipster.library.domain.TestBlog}.
 */
@RestController
@RequestMapping("/api")
public class TestBlogResource {

    private final Logger log = LoggerFactory.getLogger(TestBlogResource.class);

    private static final String ENTITY_NAME = "testBlog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestBlogService testBlogService;

    private final TestBlogRepository testBlogRepository;

    public TestBlogResource(TestBlogService testBlogService, TestBlogRepository testBlogRepository) {
        this.testBlogService = testBlogService;
        this.testBlogRepository = testBlogRepository;
    }

    /**
     * {@code POST  /test-blogs} : Create a new testBlog.
     *
     * @param testBlogDTO the testBlogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testBlogDTO, or with status {@code 400 (Bad Request)} if the testBlog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-blogs")
    public ResponseEntity<TestBlogDTO> createTestBlog(@Valid @RequestBody TestBlogDTO testBlogDTO) throws URISyntaxException {
        log.debug("REST request to save TestBlog : {}", testBlogDTO);
        if (testBlogDTO.getId() != null) {
            throw new BadRequestAlertException("A new testBlog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestBlogDTO result = testBlogService.save(testBlogDTO);
        return ResponseEntity
            .created(new URI("/api/test-blogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-blogs/:id} : Updates an existing testBlog.
     *
     * @param id the id of the testBlogDTO to save.
     * @param testBlogDTO the testBlogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testBlogDTO,
     * or with status {@code 400 (Bad Request)} if the testBlogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testBlogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-blogs/{id}")
    public ResponseEntity<TestBlogDTO> updateTestBlog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestBlogDTO testBlogDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TestBlog : {}, {}", id, testBlogDTO);
        if (testBlogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testBlogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testBlogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestBlogDTO result = testBlogService.save(testBlogDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testBlogDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-blogs/:id} : Partial updates given fields of an existing testBlog, field will ignore if it is null
     *
     * @param id the id of the testBlogDTO to save.
     * @param testBlogDTO the testBlogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testBlogDTO,
     * or with status {@code 400 (Bad Request)} if the testBlogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the testBlogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the testBlogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-blogs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TestBlogDTO> partialUpdateTestBlog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestBlogDTO testBlogDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestBlog partially : {}, {}", id, testBlogDTO);
        if (testBlogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testBlogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testBlogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestBlogDTO> result = testBlogService.partialUpdate(testBlogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testBlogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /test-blogs} : get all the testBlogs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testBlogs in body.
     */
    @GetMapping("/test-blogs")
    public List<TestBlogDTO> getAllTestBlogs() {
        log.debug("REST request to get all TestBlogs");
        return testBlogService.findAll();
    }

    /**
     * {@code GET  /test-blogs/:id} : get the "id" testBlog.
     *
     * @param id the id of the testBlogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testBlogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-blogs/{id}")
    public ResponseEntity<TestBlogDTO> getTestBlog(@PathVariable Long id) {
        log.debug("REST request to get TestBlog : {}", id);
        Optional<TestBlogDTO> testBlogDTO = testBlogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testBlogDTO);
    }

    /**
     * {@code DELETE  /test-blogs/:id} : delete the "id" testBlog.
     *
     * @param id the id of the testBlogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-blogs/{id}")
    public ResponseEntity<Void> deleteTestBlog(@PathVariable Long id) {
        log.debug("REST request to delete TestBlog : {}", id);
        testBlogService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/test-blogs?query=:query} : search for the testBlog corresponding
     * to the query.
     *
     * @param query the query of the testBlog search.
     * @return the result of the search.
     */
    @GetMapping("/_search/test-blogs")
    public List<TestBlogDTO> searchTestBlogs(@RequestParam String query) {
        log.debug("REST request to search TestBlogs for query {}", query);
        return testBlogService.search(query);
    }
}
