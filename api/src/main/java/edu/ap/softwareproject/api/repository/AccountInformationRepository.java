package edu.ap.softwareproject.api.repository;

import edu.ap.softwareproject.api.entity.AccountInformation;
import org.springframework.data.repository.CrudRepository;

public interface AccountInformationRepository extends CrudRepository<AccountInformation, Long> {
}
