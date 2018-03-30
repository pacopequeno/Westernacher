package testtask.useraccounts.config;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import testtask.useraccounts.webdriver.WebDriverBuilder;

import java.text.SimpleDateFormat;

/**
 * Base class which provides essential functionality for selenium tests.
 */
@ContextConfiguration(classes = WebdriverTestSpringConfig.class)
public abstract class AbstractSeleniumTest {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractSeleniumTest.class);

    private WebDriver webDriver;

    protected AbstractSeleniumTest() {
        // preparation of TestContextManager must be ran first to initialize Autowired fields
        try {
            new TestContextManager(getClass()).prepareTestInstance(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public final void setUp() {
        // initialize webdriver instance
        webDriver = new WebDriverBuilder().createWebDriver();
    }

    @After
    public final void tearDown() {
        // optional for debugging:
        // printBrowserConsoleLog();

        // kill webdriver instance
        if (webDriver != null) {
            webDriver.quit();
        }

        LOG.info("Successful test run :)");
    }

    protected WebDriver getWebDriver() {
        return webDriver;
    }

    public void printBrowserConsoleLog() {
        LOG.info("\n****** BROWSER CONSOLE LOG ***");
        final LogEntries logEntries = webDriver.manage().logs().get(LogType.BROWSER);
        for (final LogEntry entry : logEntries) {
            if (entry.getLevel().toString().equals("SEVERE")) { // possible types: "OFF", "SEVERE", "WARNING", "INFO", "DEBUG", "ALL"
                LOG.error(new SimpleDateFormat("HH:mm:ss").format(entry.getTimestamp()) + " - " + entry.getLevel() + ": " + entry.getMessage());
            } else {
                LOG.info(new SimpleDateFormat("HH:mm:ss").format(entry.getTimestamp()) + " - " + entry.getLevel() + ": " + entry.getMessage());
            }
        }
        LOG.info("***** END OF BROWSER CONSOLE LOG ***\n");
    }
}