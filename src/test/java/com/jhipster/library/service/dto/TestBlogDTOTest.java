package com.jhipster.library.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.library.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestBlogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestBlogDTO.class);
        TestBlogDTO testBlogDTO1 = new TestBlogDTO();
        testBlogDTO1.setId(1L);
        TestBlogDTO testBlogDTO2 = new TestBlogDTO();
        assertThat(testBlogDTO1).isNotEqualTo(testBlogDTO2);
        testBlogDTO2.setId(testBlogDTO1.getId());
        assertThat(testBlogDTO1).isEqualTo(testBlogDTO2);
        testBlogDTO2.setId(2L);
        assertThat(testBlogDTO1).isNotEqualTo(testBlogDTO2);
        testBlogDTO1.setId(null);
        assertThat(testBlogDTO1).isNotEqualTo(testBlogDTO2);
    }
}
