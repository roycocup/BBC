import junit.framework.TestCase;
import org.junit.Test;
import uk.co.rodderscode.bbc.Fetcher;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.Assert.*;

public class TestInput{

    @Test
    public void canSetStdIn()
    {
        String expected = "this is a string";
        InputStream inputStream = new ByteArrayInputStream(expected.getBytes());

        System.setIn(inputStream);

        Fetcher fetcher = new Fetcher();
        String inputText = fetcher.getInput();

        assertEquals(expected, inputText);

    }

}
