package be.pxl.researchproject.repository;

import be.pxl.researchproject.domain.Horse;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorseRepository extends JpaRepository<Horse, Long> {
    Optional<Horse> findById(Long id);
}
