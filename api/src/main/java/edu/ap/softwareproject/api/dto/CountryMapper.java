package edu.ap.softwareproject.api.dto;

import edu.ap.softwareproject.api.entity.Country;

import java.util.function.Function;

public class CountryMapper implements Function<Country, CountryDTO> {

    @Override
    public CountryDTO apply(Country country) {
        return new CountryDTO(
                country.getId(),
                country.getIso(),
                country.getNicename()
        );
    }
}
