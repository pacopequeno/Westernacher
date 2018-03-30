package testtask.useraccounts.page;

import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testtask.useraccounts.config.TestEnvironmentConfig;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.By.cssSelector;

/**
 * Base class for the PageObjects which does common initialization.
 */

public abstract class AbstractPage {

    private final WebDriver webDriver;
    private final TestEnvironmentConfig config;

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractPage.class);
    protected static final int DEFAULT_TIMEOUT = 10;


    protected AbstractPage(final WebDriver webDriver, final TestEnvironmentConfig config) {
        PageFactory.initElements(webDriver, this);
        this.webDriver = webDriver;
        this.config = config;
    }

    protected WebDriver getWebDriver() {
        return webDriver;
    }

    protected TestEnvironmentConfig getConfig() {
        return config;
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

    protected <T> T waitUntil(final Function<WebDriver, T> isTrue) {
    	return getDefaultWait().until(isTrue);
    }

    protected <T> T waitUntil(final Function<WebDriver, T> isTrue, final int timeoutSeconds) {
    	return getWait().withTimeout(timeoutSeconds, TimeUnit.SECONDS).until(isTrue);
    }

    public String getPageTitle() {
        return webDriver.getTitle();
    }

    protected WebElement findElement(final By by) {
        return getWebDriver().findElement(by);
    }

    protected List<WebElement> findElementsWithCss(final String cssLocator) {
        return getWebDriver().findElements(cssSelector(cssLocator));
    }

    protected boolean webElementAppears(final WebElement element) {
    	try {
            return waitUntil(PageConditions.appearsVisible(element));
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean isWebElementDisplayedWithWait(final WebElement element) {
        return useImplicitWait(locator -> {
            try {
                return locator.isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        }, element);
    }

    protected boolean isWebElementDisplayed(final WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected boolean isWebElementNotDisplayed(final WebElement element) {
        try {
            return !element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return true;
        }
    }

    protected boolean isWebElementDisplayed(final By element) {
        try {
            WebElement webElement = getWebDriver().findElement(element);
            return webElement.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected boolean isWebElementEnabled(final WebElement element) {
        try {
            return element.isEnabled();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected boolean isWebElementDisabled(final WebElement element) {
        try {
            return !element.isEnabled();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return true;
        }
    }

    protected void scrollToElement(final WebElement element) {
        Actions actions = new Actions(webDriver);
        actions.moveToElement(element);
        actions.perform();
    }

    protected void fillInputField(final WebElement element, final String text) {
        webElementAppears(element);
        waitUntil(ExpectedConditions.elementToBeClickable(element));
        element.clear();
        element.sendKeys(text);
    }

    protected String getTextFromWebElement(final WebElement element) {
    	waitUntil(PageConditions.appearsVisible(element));
        return element.getText();
    }

    protected String getTextFromSelectedOptionInWebelement(final WebElement element) {
        waitUntil(PageConditions.appearsVisible(element));
        return new Select(element).getFirstSelectedOption().getText();
    }

    protected String getAttributeValueFromWebElement(final WebElement element) {
    	waitUntil(PageConditions.appearsVisible(element));
        return element.getAttribute("value");
    }

    protected String getAttributeTitleFromWebElement(final WebElement element) {
    	waitUntil(PageConditions.appearsVisible(element));
        return element.getAttribute("title");
    }

    protected String getAttributeSrcFromWebElement(final WebElement element) {
    	waitUntil(PageConditions.appearsVisible(element));
        return element.getAttribute("src");
    }

    protected String getAttributeClassFromWebElement(final WebElement element) {
    	waitUntil(PageConditions.appearsVisible(element));
        return element.getAttribute("class");
    }

    protected String getAttributeHrefFromWebElement(final WebElement element) {
    	waitUntil(PageConditions.appearsVisible(element));
        return element.getAttribute("href");
    }

    protected void selectOptionOfWebElementByVisibleText(final WebElement element, final String visibleText) {
        webElementAppears(element);
    	waitUntil(ExpectedConditions.elementToBeClickable(element));
    	new Select(element).selectByVisibleText(visibleText);
    }

    protected void selectOptionOfWebElementByValue(final WebElement element, final String value) {
        webElementAppears(element);
    	waitUntil(ExpectedConditions.elementToBeClickable(element));
    	new Select(element).selectByValue(value);
    }

    protected void click(final WebElement element) {
//        webElementAppears(element);
    	waitUntil(ExpectedConditions.elementToBeClickable(element));
    	element.click();
    }
    private <T> boolean useImplicitWait(java.util.function.Predicate<T> code, T locator) {
        try {
            getWebDriver().manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            return code.test(locator);
        } finally {
            getWebDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        }
    }
}