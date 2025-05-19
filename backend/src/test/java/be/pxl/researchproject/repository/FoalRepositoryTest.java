package be.pxl.researchproject.repository;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import be.pxl.researchproject.builder.FoalBuilder;
import be.pxl.researchproject.domain.Foal;
import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
@Rollback
class FoalRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FoalRepository foalRepository;

    private Foal foal1 = FoalBuilder.aFoal().withName("Crystal Cascade")
            .withBirthDate(LocalDate.of(2023, 5, 23))
            .build();;
    private Foal foal2 = FoalBuilder.aFoal().withName("Aurora Breeze")
            .withBirthDate(LocalDate.of(2023, 6, 23))
            .build();;

    @BeforeEach
    void init() {
        foalRepository.saveAll(List.of(foal1, foal2));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testFindByIdValidIdReturnsFoal() {
        Optional<Foal> foundFoal = foalRepository.findById(foal1.getId());
        assertTrue(foundFoal.isPresent());
        assertEquals(foal1.getName(), foundFoal.get().getName());
        assertEquals(foal1.getAge(), foundFoal.get().getAge());
        assertEquals(foal1.getBirthDate(), foundFoal.get().getBirthDate());
    }

    @Test
    void testFindAllReturnsAllFoals() {
        List<Foal> allFoals = foalRepository.findAll();
        assertEquals(2, allFoals.size());
        assertTrue(allFoals.stream().anyMatch(foal -> foal.getName().equals("Crystal Cascade")));
        assertTrue(allFoals.stream().anyMatch(foal -> foal.getName().equals("Aurora Breeze")));
    }

    @Test
    void testSaveNewFoalSavesFoal() {
        Foal newFoal = FoalBuilder.aFoal().withName("Sunbeam Serenade")
                .withBirthDate(LocalDate.of(2023, 7, 23))
                .build();
        Foal savedFoal = foalRepository.save(newFoal);
        assertNotNull(savedFoal.getId());
        assertEquals(newFoal.getName(), savedFoal.getName());
        assertEquals(newFoal.getAge(), savedFoal.getAge());
        assertEquals(newFoal.getBirthDate(), savedFoal.getBirthDate());
    }

    @Test
    void testSaveExistingFoalUpdatesFoal() {
        foal1.setName("Mystic Meadow");
        foal1.setBirthDate(LocalDate.of(2023, 8, 23));
        Foal updatedFoal = foalRepository.save(foal1);
        assertEquals(foal1.getId(), updatedFoal.getId());
        assertEquals(foal1.getName(), updatedFoal.getName());
        assertEquals(foal1.getAge(), updatedFoal.getAge());
        assertEquals(foal1.getBirthDate(), updatedFoal.getBirthDate());
    }

    @Test
    void testDeleteExistingFoalDeletesFoal() {
        foalRepository.deleteById(foal1.getId());
        Optional<Foal> deletedFoal = foalRepository.findById(foal1.getId());
        assertTrue(deletedFoal.isEmpty());
    }
}
