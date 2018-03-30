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
import java.util.List;

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
        System.out.println("\n****** BROWSER CONSOLE LOG ***");
        final LogEntries logEntries = webDriver.manage().logs().get(LogType.BROWSER);
        for (final LogEntry entry : logEntries) {
            if (entry.getLevel().toString().equals("SEVERE")) { // possible types: "OFF", "SEVERE", "WARNING", "INFO", "DEBUG", "ALL"
                System.err.println(new SimpleDateFormat("HH:mm:ss").format(entry.getTimestamp()) + " - " + entry.getLevel() + ": " + entry.getMessage());
            } else {
                System.out.println(new SimpleDateFormat("HH:mm:ss").format(entry.getTimestamp()) + " - " + entry.getLevel() + ": " + entry.getMessage());
            }
        }
        System.out.println("***** END OF BROWSER CONSOLE LOG ***\n");
    }

    protected WebElement findElementById(final String id) {
        return findElementWithCss("[id = '" + id + "']");
    }

    protected WebElement findElementWithCss(final String cssLocator) {
        return getWebDriver().findElement(By.cssSelector(cssLocator));
    }

    protected List<WebElement> findElementsWithCss(final String cssLocator) {
        return getWebDriver().findElements(By.cssSelector(cssLocator));
    }
}