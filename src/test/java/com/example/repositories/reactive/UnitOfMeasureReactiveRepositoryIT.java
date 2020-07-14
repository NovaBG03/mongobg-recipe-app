package com.example.repositories.reactive;

import com.example.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureReactiveRepositoryIT {

    private static final String DESCRIPTION = "description";

    @Autowired
    UnitOfMeasureReactiveRepository reactiveRepository;

    @Before
    public void setUp() {
        reactiveRepository.deleteAll().block();
    }

    @Test
    public void testUomSave() {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription(DESCRIPTION);

        reactiveRepository.save(uom).block();

        Long count = reactiveRepository.count().block();

        assertEquals(Long.valueOf(1L), count);
    }

    @Test
    public void testFindByDescription() {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription(DESCRIPTION);

        reactiveRepository.save(uom).block();

        UnitOfMeasure found = reactiveRepository.findByDescription(DESCRIPTION).block();

        assertEquals(DESCRIPTION, found.getDescription());
    }
}
