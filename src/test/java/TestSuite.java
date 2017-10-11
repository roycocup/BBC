import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//JUnit Suite Test
@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestMain.class,
        TestInput.class,
        TestValidator.class,
        TestHttpFetcher.class,
})

public class TestSuite {

}