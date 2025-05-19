package be.pxl.researchproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import be.pxl.researchproject.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
