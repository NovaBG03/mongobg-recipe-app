package com.example.repositories.reactive;

import com.example.domain.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CategoryReactiveRepositoryIT {

    private static final String DESCRIPTION = "description";

    @Autowired
    CategoryReactiveRepository reactiveRepository;

    @Before
    public void setUp() {
        reactiveRepository.deleteAll().block();
    }

    @Test
    public void testSaveCategory() {
        Category category = new Category();
        category.setDescription(DESCRIPTION);

        reactiveRepository.save(category).block();

        Long count = reactiveRepository.count().block();

        assertEquals(Long.valueOf(1L), count);
    }

    @Test
    public void testFindByDescription() {
        Category category = new Category();
        category.setDescription(DESCRIPTION);

        reactiveRepository.save(category).block();

        Category found = reactiveRepository.findByDescription(DESCRIPTION).block();

        assertEquals(DESCRIPTION, found.getDescription());
    }
}
