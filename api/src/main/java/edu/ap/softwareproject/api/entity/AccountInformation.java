package edu.ap.softwareproject.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A user's account information.
 */
@Entity
@Table(name = "account_information")
@Data
@NoArgsConstructor
public class AccountInformation {
    @Id
    private long accounts_id;
    private String company_name;
    private String contact_firstname;
    private String contact_lastname;
    private String contact_telephone;
    @Column(name = "country")
    private long countryId;
    @JoinColumn(name = "country", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Country country;
    private String street;
    private String city;
    private short postal_code;
    private short street_number;
    private String box;
    private String street_secondary;
    private String btw_number;
    public AccountInformation(String company_name, String contact_firstname, String contact_lastname, String contact_telephone, Integer country, String street, String city, short postal_code, short street_number, String box, String street_secondary, String btw_number) {
        this.company_name = company_name;
        this.contact_firstname = contact_firstname;
        this.contact_lastname = contact_lastname;
        this.contact_telephone = contact_telephone;
        this.countryId = country;
        this.street = street;
        this.city = city;
        this.postal_code = postal_code;
        this.street_number = street_number;
        this.box = box;
        this.street_secondary = street_secondary;
        this.btw_number = btw_number;
    }
}
