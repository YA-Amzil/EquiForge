package be.pxl.researchproject.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be.pxl.researchproject.domain.Foal;

@Repository
public interface FoalRepository extends JpaRepository<Foal, Long> {
    Optional<Foal> findById(Long id);
}
