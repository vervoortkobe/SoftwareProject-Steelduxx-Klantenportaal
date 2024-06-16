package edu.ap.softwareproject.api.controller;

import edu.ap.softwareproject.api.dto.CountryDTO;
import edu.ap.softwareproject.api.dto.CountryMapper;
import edu.ap.softwareproject.api.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

  private final CountryService countryService;
  private final CountryMapper countryMapper;

  public CountryController(CountryService countryService) {
    this.countryService = countryService;
    this.countryMapper = new CountryMapper();
  }

  @Operation(summary = "Retrieves all countries valid in registration.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "A list of countries."),
  })
  @GetMapping("")
  public ResponseEntity<List<CountryDTO>> getAllCountries() {
    return new ResponseEntity<>(countryService.getAllCountries().stream().map(
        countryMapper).toList(), HttpStatus.OK);
  }
}
