package uk.co.rodderscode.bbc;

import org.apache.commons.validator.routines.UrlValidator;

public class Validator {

    public static Boolean validateUrl(String url) {
        String[] schemes = {"http","https"}; // DEFAULT schemes = "http", "https", "ftp"
        UrlValidator urlValidator = new org.apache.commons.validator.routines.UrlValidator(schemes);

        return urlValidator.isValid(url);
    }


}
