package testtask.useraccounts.page;

import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Base class for the PageObjects which does common initialization.
 */

public abstract class AbstractPage {

    private final WebDriver webDriver;
    protected static final int DEFAULT_TIMEOUT = 10;


    protected AbstractPage(final WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.webDriver = webDriver;
    }

    protected WebDriver getWebDriver() {
        return webDriver;
    }

    public String getPageTitle() {
        return webDriver.getTitle();
    }

    public WebElement findElementById(final String id) {
        return findElementWithCss("[id = '" + id + "']");
    }

    public WebElement findElementWithCss(final String cssLocator) {
        return getWebDriver().findElement(By.cssSelector(cssLocator));
    }

    protected List<WebElement> findElementsWithCss(final String cssLocator) {
        return getWebDriver().findElements(By.cssSelector(cssLocator));
    }

    protected final FluentWait<WebDriver> getDefaultWait() {
        return new FluentWait<WebDriver>(
                getWebDriver())
                .withTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .pollingEvery(200, TimeUnit.MILLISECONDS)
                .ignoring(UnhandledAlertException.class);
    }

    protected final FluentWait<WebDriver> getWait() {
        return new FluentWait<WebDriver>(getWebDriver())
                .pollingEvery(200, TimeUnit.MILLISECONDS)
                .ignoring(UnhandledAlertException.class);
    }

    protected <T> T waitUntil(final Function<WebDriver, T> isTrue, final int timeoutSeconds) {
    	return getWait().withTimeout(timeoutSeconds, TimeUnit.SECONDS).until(isTrue);
    }

    protected boolean waitUntilWebElementAppears(final WebElement element) {
    	try {
            return waitUntil(PageConditions.appearsVisible(element), DEFAULT_TIMEOUT);
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected void waitUntilWebElementIsClickable(final WebElement element) {
        waitUntilWebElementAppears(element);
        waitUntil(ExpectedConditions.elementToBeClickable(element), DEFAULT_TIMEOUT);
    }

    protected void clearInputField(final WebElement element) {
        waitUntilWebElementIsClickable(element);
        element.clear();
        element.sendKeys(Keys.RETURN);
    }

    protected void fillInputField(final WebElement element, final String text) {
        clearInputField(element);
        element.sendKeys(text);
    }

    protected String getTextFromWebElement(final WebElement element) {
        waitUntilWebElementAppears(element);
        return element.getText();
    }

    protected void click(final WebElement element) {
        waitUntilWebElementIsClickable(element);
    	element.click();
    }
}