package com.jhipster.library.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.jhipster.library.domain.TestBlog;
import com.jhipster.library.repository.TestBlogRepository;
import com.jhipster.library.repository.search.TestBlogSearchRepository;
import com.jhipster.library.service.dto.TestBlogDTO;
import com.jhipster.library.service.mapper.TestBlogMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestBlog}.
 */
@Service
@Transactional
public class TestBlogService {

    private final Logger log = LoggerFactory.getLogger(TestBlogService.class);

    private final TestBlogRepository testBlogRepository;

    private final TestBlogMapper testBlogMapper;

    private final TestBlogSearchRepository testBlogSearchRepository;

    public TestBlogService(
        TestBlogRepository testBlogRepository,
        TestBlogMapper testBlogMapper,
        TestBlogSearchRepository testBlogSearchRepository
    ) {
        this.testBlogRepository = testBlogRepository;
        this.testBlogMapper = testBlogMapper;
        this.testBlogSearchRepository = testBlogSearchRepository;
    }

    /**
     * Save a testBlog.
     *
     * @param testBlogDTO the entity to save.
     * @return the persisted entity.
     */
    public TestBlogDTO save(TestBlogDTO testBlogDTO) {
        log.debug("Request to save TestBlog : {}", testBlogDTO);
        TestBlog testBlog = testBlogMapper.toEntity(testBlogDTO);
        testBlog = testBlogRepository.save(testBlog);
        TestBlogDTO result = testBlogMapper.toDto(testBlog);
        testBlogSearchRepository.save(testBlog);
        return result;
    }

    /**
     * Partially update a testBlog.
     *
     * @param testBlogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestBlogDTO> partialUpdate(TestBlogDTO testBlogDTO) {
        log.debug("Request to partially update TestBlog : {}", testBlogDTO);

        return testBlogRepository
            .findById(testBlogDTO.getId())
            .map(
                existingTestBlog -> {
                    testBlogMapper.partialUpdate(existingTestBlog, testBlogDTO);
                    return existingTestBlog;
                }
            )
            .map(testBlogRepository::save)
            .map(
                savedTestBlog -> {
                    testBlogSearchRepository.save(savedTestBlog);

                    return savedTestBlog;
                }
            )
            .map(testBlogMapper::toDto);
    }

    /**
     * Get all the testBlogs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TestBlogDTO> findAll() {
        log.debug("Request to get all TestBlogs");
        return testBlogRepository.findAll().stream().map(testBlogMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one testBlog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestBlogDTO> findOne(Long id) {
        log.debug("Request to get TestBlog : {}", id);
        return testBlogRepository.findById(id).map(testBlogMapper::toDto);
    }

    /**
     * Delete the testBlog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestBlog : {}", id);
        testBlogRepository.deleteById(id);
        testBlogSearchRepository.deleteById(id);
    }

    /**
     * Search for the testBlog corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TestBlogDTO> search(String query) {
        log.debug("Request to search TestBlogs for query {}", query);
        return StreamSupport
            .stream(testBlogSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(testBlogMapper::toDto)
            .collect(Collectors.toList());
    }
}
