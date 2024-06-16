package edu.ap.softwareproject.api.repository;

import edu.ap.softwareproject.api.entity.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Long> {
}
