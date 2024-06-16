package edu.ap.softwareproject.api.service;

import edu.ap.softwareproject.api.entity.Country;
import edu.ap.softwareproject.api.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A service used by the registration page for requesting/validating countries.
 */
@Service
public class CountryService {
  private final CountryRepository countryRepository;

  public CountryService(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
  }

  public List<Country> getAllCountries() {
    return (List<Country>) countryRepository.findAll();
  }

  public Boolean countryExistsById(long id) {
    return countryRepository.findById(id).isPresent();
  }
}
