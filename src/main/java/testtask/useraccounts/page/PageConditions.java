package testtask.useraccounts.page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Set of static methods to create useful conditions for selenium tests.
 * These conditions complements the selenium {@see ExpectedConditions}.
 */

public final class PageConditions {

	private static final Logger LOG = Logger.getLogger(PageConditions.class.getName());

	private PageConditions() {}
	
	/**
	 * An expectation for checking the located element appears in the DOM an is visible.
	 * @param locator locator for the element to test
	 * @return created expectation
	 */
	public static ExpectedCondition<Boolean> appearsVisible(final By locator) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return elementIfVisible(findElement(locator, driver)) != null;
				} catch (StaleElementReferenceException e) {
					return Boolean.FALSE;
				} catch (NoSuchElementException e){
					return Boolean.FALSE;
				}
			}

			@Override
			public String toString() {
				return "visibility of element located by " + locator;
			}
		};
	}

	/**
	 * An expectation for checking the element appears in the DOM is visible.
	 */
	public static ExpectedCondition<Boolean> appearsVisible(final WebElement element) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return elementIfVisible(element) != null;
				} catch (StaleElementReferenceException e) {
					return Boolean.FALSE;
				} catch (NoSuchElementException e){
					return Boolean.FALSE;
				}
			}

			@Override
			public String toString() {
				return "visibility of element " + element;
			}
		};
	}

	/**
	 * An expectation for checking an element is disappeared. 
	 * Expectation is reached if the element is not displayed or no longer connected to the DOM.
	 */
	public static ExpectedCondition<Boolean> disappeared(final WebElement element) {
		return new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return !element.isDisplayed();
				} catch(NoSuchElementException e) {
					return true;
				} catch(StaleElementReferenceException e) {
					return true;
				}
			}
			
			@Override
			public String toString() {
				return "disappearing of element " + element;
			}
		};
	}
	
	private static WebElement elementIfVisible(WebElement element) {
		return element.isDisplayed() ? element : null;
	}

	private static WebElement findElement(By by, WebDriver driver) {
		try {
			return driver.findElement(by);
		} catch (NoSuchElementException e) {
			throw e;
		} catch (WebDriverException e) {
			LOG.log(Level.WARNING, String.format("WebDriverException thrown by findElement(%s)", by), e);
			throw e;
		}
	}
}