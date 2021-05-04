package com.jhipster.library.repository.search;

import com.jhipster.library.domain.TestBlog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TestBlog} entity.
 */
public interface TestBlogSearchRepository extends ElasticsearchRepository<TestBlog, Long> {}
