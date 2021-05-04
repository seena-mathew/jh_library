package com.jhipster.library.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.library.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestBlogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestBlog.class);
        TestBlog testBlog1 = new TestBlog();
        testBlog1.setId(1L);
        TestBlog testBlog2 = new TestBlog();
        testBlog2.setId(testBlog1.getId());
        assertThat(testBlog1).isEqualTo(testBlog2);
        testBlog2.setId(2L);
        assertThat(testBlog1).isNotEqualTo(testBlog2);
        testBlog1.setId(null);
        assertThat(testBlog1).isNotEqualTo(testBlog2);
    }
}
