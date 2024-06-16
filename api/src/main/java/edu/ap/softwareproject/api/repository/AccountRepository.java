package edu.ap.softwareproject.api.repository;

import edu.ap.softwareproject.api.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByEmail(@Param("email") String email);
    Optional<Account> deleteAccountByEmail(@Param("email") String email);
}
