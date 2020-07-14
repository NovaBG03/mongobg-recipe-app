package com.example.repositories.reactive;

import com.example.domain.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class RecipeReactiveRepositoryIT {

    @Autowired
    RecipeReactiveRepository reactiveRepository;

    @Before
    public void setUp() {
        reactiveRepository.deleteAll().block();
    }

    @Test
    public void testSaveRecipe() {
        Recipe recipe = new Recipe();

        reactiveRepository.save(recipe).block();

        Long count = reactiveRepository.count().block();

        assertEquals(Long.valueOf(1L), count);
    }
}
