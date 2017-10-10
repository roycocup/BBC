import org.junit.Before;
import org.junit.Test;
import uk.co.rodderscode.bbc.Validator;

import static org.junit.Assert.assertFalse;

public class TestValidator {


    @Test
    public void assertOnlyGoodUrlsPassValidation()
    {

        // bad ones
        assertFalse (Validator.validateUrl("lksdjfsflk"));
        assertFalse (Validator.validateUrl("htt://rodderscode.co.uk"));
        assertFalse (Validator.validateUrl("http://rodderscode.c"));
        assertFalse (Validator.validateUrl("http://rodderscode.java.cofres"));
        assertFalse (Validator.validateUrl("htp://bbc.co.uk"));
        assertFalse (Validator.validateUrl("ftp://bbc.co.uk"));

        //good ones
        assert (Validator.validateUrl("http://yahoo.com"));
        assert (Validator.validateUrl("http://bbc.co.uk"));
        assert (Validator.validateUrl("https://bbc.co.uk"));
        assert (Validator.validateUrl("http://bbc.co.uk/link/doesnt/exist"));
        assert (Validator.validateUrl("http://rodderscode.co.uk"));
        assert (Validator.validateUrl("http://www.rodderscode.co"));

    }
}
