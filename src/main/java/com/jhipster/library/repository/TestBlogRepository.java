package com.jhipster.library.repository;

import com.jhipster.library.domain.TestBlog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TestBlog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestBlogRepository extends JpaRepository<TestBlog, Long> {}
