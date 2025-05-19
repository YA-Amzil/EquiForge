package be.pxl.researchproject.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.pxl.researchproject.domain.ResetPassword;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {
    Optional<ResetPassword> findByToken(String token);
}