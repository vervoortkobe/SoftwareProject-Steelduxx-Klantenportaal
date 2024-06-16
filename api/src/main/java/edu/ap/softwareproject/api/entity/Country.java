package edu.ap.softwareproject.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * A country item containing information for the registry page.
 */
@Getter
@Entity
@Table(name = "country")
public class Country {
    @Id
    private long id;
    private String iso;
    private String name;
    private String nicename;
    private String iso3;
    private Integer numcode;
    private Integer phonecode;

    public void setId(long id) {
        this.id = id;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public void setNumcode(Integer numcode) {
        this.numcode = numcode;
    }

    public void setPhonecode(Integer phonecode) {
        this.phonecode = phonecode;
    }

    public Country(long id, String iso, String name, String nicename, String iso3, Integer numcode, Integer phonecode) {
        this.id = id;
        this.iso = iso;
        this.name = name;
        this.nicename = nicename;
        this.iso3 = iso3;
        this.numcode = numcode;
        this.phonecode = phonecode;
    }

    public Country() {
    }
}
