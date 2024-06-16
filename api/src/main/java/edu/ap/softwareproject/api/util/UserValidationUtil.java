package edu.ap.softwareproject.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UserValidationUtil {
    private UserValidationUtil() {

    }
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_INTERNATIONAL_PHONE_NUMBER_REGEX =
            Pattern.compile("^[1-9][0-9]{3,14}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_EUROPEAN_VAT_REGEX =
            Pattern.compile("""
                    (?xi)^(
                    (AT)?U\\d{8} |                              # Austria
                    (BE)?0\\d{9} |                              # Belgium
                    (BG)?\\d{9,10} |                            # Bulgaria
                    (HR)?\\d{11} |                              # Croatia
                    (CY)?\\d{8}[A-Z] |                          # Cyprus
                    (CZ)?\\d{8,10} |                            # Czech Republic
                    (DE)?\\d{9} |                               # Germany
                    (DK)?\\d{8} |                               # Denmark
                    (EE)?\\d{9} |                               # Estonia
                    (EL)?\\d{9} |                               # Greece
                    ES[A-Z]\\d{7}(?:\\d|[A-Z]) |              # Spain
                    (FI)?\\d{8} |                               # Finland
                    (FR)?[0-9A-Z]{2}[0-9]{9} |                    # France
                    (GB)?(\\d{9}(\\d]{3})?|[A-Z]{2}\\d{3}) | # United Kingdom
                    (HU)?\\d{8} |                               # Hungary
                    (IE)?\\d{7}[A-Z]{1,2}   |                   # Ireland
                    (IE)?\\d[A-Z]\\d{5}[A-Z] |                # Ireland (2)
                    (IT)?\\d{11} |                              # Italy
                    (LT)?(\\d{9}|\\d{12}) |                   # Lithuania
                    (LU)?\\d{8} |                               # Luxembourg
                    (LV)?\\d{11} |                              # Latvia
                    (MT)?\\d{8} |                               # Malta
                    (NL)?\\d{9}B[0-9]{2} |                      # Netherlands
                    (PL)?\\d{10} |                              # Poland
                    (PT)?\\d{9} |                               # Portugal
                    (RO)?\\d{2,10} |                            # Romania
                    (SE)?\\d{12} |                              # Sweden
                    (SI)?\\d{8} |                               # Slovenia
                    (SK)?\\d{10}                                # Slovakia
                    )$""");

    /**
     * Validates an email address with a regex.
     * @param emailStr The email to check.
     * @return If the email is valid
     */
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    /**
     * Validates an international phone number.
     * @param phoneStr The phone number to validate
     * @return If the phone number is valid.
     */
    public static boolean validatePhoneNumber(String phoneStr) {
        Matcher matcher = VALID_INTERNATIONAL_PHONE_NUMBER_REGEX.matcher(phoneStr);
        return matcher.matches();
    }

    /**
     * Validates an European VAT number.
     * @param vatStr The VAT number
     * @return If the VAT number is valid.
     */
    public static boolean validateVAT(String vatStr) {
        Matcher matcher = VALID_EUROPEAN_VAT_REGEX.matcher(vatStr);
        return matcher.matches();
    }
}
