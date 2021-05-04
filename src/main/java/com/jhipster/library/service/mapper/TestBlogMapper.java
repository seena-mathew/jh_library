package com.jhipster.library.service.mapper;

import com.jhipster.library.domain.*;
import com.jhipster.library.service.dto.TestBlogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TestBlog} and its DTO {@link TestBlogDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TestBlogMapper extends EntityMapper<TestBlogDTO, TestBlog> {}
