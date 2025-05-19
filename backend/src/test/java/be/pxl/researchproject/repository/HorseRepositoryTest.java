package be.pxl.researchproject.repository;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import be.pxl.researchproject.builder.HorseBuilder;
import be.pxl.researchproject.domain.Horse;
import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
@Rollback
class HorseRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private HorseRepository horseRepository;

    private Horse horse1 = new HorseBuilder()
            .withName("Shadow Whisperer")
            .withBirthDate(LocalDate.of(2010, 5, 23))
            .withHeight(1.7)
            .isPregnant(false)
            .withStudHorse("Moonlit Sprite")
            .build();

    private Horse horse2 = new HorseBuilder()
            .withName("Wildfire Dreamer")
            .withBirthDate(LocalDate.of(2015, 6, 10))
            .withHeight(1.6)
            .isPregnant(true)
            .withDatePregnant(LocalDate.of(2023, 4, 15))
            .withStudHorse("Sunlit Serenade")
            .build();

    @BeforeEach
    void init() {
        horseRepository.saveAll(List.of(horse1, horse2));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testFindByIdValidIdReturnsHorse() {
        Optional<Horse> foundHorse = horseRepository.findById(horse1.getId());
        assertTrue(foundHorse.isPresent());
        assertEquals(horse1.getName(), foundHorse.get().getName());
        assertEquals(horse1.getBirthDate(), foundHorse.get().getBirthDate());
        assertEquals(horse1.getHeight(), foundHorse.get().getHeight());
        assertEquals(horse1.isPregnant(), foundHorse.get().isPregnant());
        assertEquals(horse1.getStudHorse(), foundHorse.get().getStudHorse());
    }

    @Test
    void testFindAllReturnsAllHorses() {
        List<Horse> allHorses = horseRepository.findAll();
        assertEquals(2, allHorses.size());
        assertTrue(allHorses.stream().anyMatch(horse -> horse.getName().equals("Shadow Whisperer")));
        assertTrue(allHorses.stream().anyMatch(horse -> horse.getName().equals("Wildfire Dreamer")));
    }

    @Test
    void testSaveNewHorseSavesHorse() {
        Horse newHorse = new HorseBuilder()
                .withName("Crystal Cascade")
                .withBirthDate(LocalDate.of(2020, 8, 15))
                .withHeight(1.8)
                .isPregnant(true)
                .withDatePregnant(LocalDate.of(2024, 2, 20))
                .withStudHorse("Big Stallion")
                .build();
        Horse savedHorse = horseRepository.save(newHorse);
        assertNotNull(savedHorse.getId());
        assertEquals(newHorse.getName(), savedHorse.getName());
        assertEquals(newHorse.getBirthDate(), savedHorse.getBirthDate());
        assertEquals(newHorse.getHeight(), savedHorse.getHeight());
        assertEquals(newHorse.isPregnant(), savedHorse.isPregnant());
        assertEquals(newHorse.getStudHorse(), savedHorse.getStudHorse());
    }

    @Test
    void testSaveExistingHorseUpdatesHorse() {
        horse1.setName("");
        horse1.setHeight(1.9);
        horse1.setStudHorse("Giant Stallion");
        Horse updatedHorse = horseRepository.save(horse1);
        assertEquals(horse1.getId(), updatedHorse.getId());
        assertEquals(horse1.getName(), updatedHorse.getName());
        assertEquals(horse1.getBirthDate(), updatedHorse.getBirthDate());
        assertEquals(horse1.getHeight(), updatedHorse.getHeight());
        assertEquals(horse1.isPregnant(), updatedHorse.isPregnant());
        assertEquals(horse1.getStudHorse(), updatedHorse.getStudHorse());
    }

    @Test
    void testDeleteExistingHorseDeletesHorse() {
        horseRepository.deleteById(horse1.getId());
        Optional<Horse> deletedHorse = horseRepository.findById(horse1.getId());
        assertTrue(deletedHorse.isEmpty());
    }
}
