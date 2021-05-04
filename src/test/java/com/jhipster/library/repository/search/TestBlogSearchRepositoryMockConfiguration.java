package com.jhipster.library.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link TestBlogSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TestBlogSearchRepositoryMockConfiguration {

    @MockBean
    private TestBlogSearchRepository mockTestBlogSearchRepository;
}