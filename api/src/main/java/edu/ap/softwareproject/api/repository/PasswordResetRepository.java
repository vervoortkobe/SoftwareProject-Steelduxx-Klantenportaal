package edu.ap.softwareproject.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import edu.ap.softwareproject.api.entity.PasswordResetToken;

@Repository
public interface PasswordResetRepository extends CrudRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByUserId(Long id);

    @NonNull
    List<PasswordResetToken> findAll();
}
