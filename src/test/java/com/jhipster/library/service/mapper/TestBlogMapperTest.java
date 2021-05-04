package com.jhipster.library.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestBlogMapperTest {

    private TestBlogMapper testBlogMapper;

    @BeforeEach
    public void setUp() {
        testBlogMapper = new TestBlogMapperImpl();
    }
}
