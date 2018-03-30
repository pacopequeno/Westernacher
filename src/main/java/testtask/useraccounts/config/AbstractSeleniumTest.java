package testtask.useraccounts.config;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnableToSetCookieException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import testtask.useraccounts.page.UserAccountsPage;
import testtask.useraccounts.webdriver.WebDriverBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Base class which provides essential functionality for selenium tests.
 */
@ContextConfiguration(classes = WebdriverTestSpringConfig.class)
public abstract class AbstractSeleniumTest {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractSeleniumTest.class);

    @Autowired
    private TestEnvironmentConfig config;
    private WebDriver webDriver;

    @Rule
    public TestRule webDriverRules;

    protected UserAccountsPage userAccountsPage;

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

        // initialize page object to be tested
        userAccountsPage = new UserAccountsPage(getWebDriver(), config);
    }

    @After
    public final void tearDown() {
        // optional for debugging:
        // printBrowserConsoleLog();

        if (webDriver != null) {
            webDriver.quit();
        }
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

    protected void setCookie(final String identifier, final String name, final String value, final String domain, final String path, final Date expiryDate) {
        final Cookie cookie = new Cookie(name, value, domain, path, expiryDate);
        try {
            webDriver.manage().addCookie(cookie);
            LOG.info(identifier + " has been set via addCookie(): " + cookie);
        } catch(UnableToSetCookieException e) {
            ((JavascriptExecutor) webDriver).executeScript("document.cookie = '" + cookie + "'");
            LOG.info(identifier + " has been set via executeScript(): " + cookie);
        }
    }

    protected void waitASecond(final int seconds) {
        final int millis = seconds * 1000;
        waitAMilliSecond(millis);
    }

    protected void waitAMilliSecond(final int milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}