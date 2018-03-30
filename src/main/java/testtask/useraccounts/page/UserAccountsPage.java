package testtask.useraccounts.page;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UserAccountsPage extends AbstractPage {

    @FindBy(css = "body > div > div > div > h1")
    private WebElement headline;

    @FindBy(css = "[id = 'accounts-table_filter'] input")
    private WebElement inputSearch;

    @FindBy(css = "[id = 'first-name']")
    private WebElement inputFirstName;
    @FindBy(css = "[id = 'last-name']")
    private WebElement inputLastName;
    @FindBy(css = "[id = 'email']")
    private WebElement inputEmail;
    @FindBy(css = "[id = 'date-of-birth']")
    private WebElement inputDateOfBirth;
    @FindBy(css = ".btn-primary")
    private WebElement buttonAddAccount;

    public UserAccountsPage(final WebDriver driver) {
        super(driver);
    }

    public void waitUntilUserAccountsPageIsDisplayed() {
        waitUntil(PageConditions.appearsVisible(headline), DEFAULT_TIMEOUT);
    }

    public String getHeadline() {
        waitUntilUserAccountsPageIsDisplayed();
        return getTextFromWebElement(headline);
    }

    public void searchTableForString(final String searchString) {
        fillInputField(inputSearch, searchString);
    }

    public void clearSearchField() {
        clearInputField(inputSearch);
        inputSearch.sendKeys(Keys.RETURN);
    }

    public void setFirstName(final String firstName) {
        fillInputField(inputFirstName, firstName);
    }

    public void setLastName(final String lastName) {
        fillInputField(inputLastName, lastName);
    }

    public void setEmail(final String email) {
        fillInputField(inputEmail, email);
    }

    public void setDateOfBirth(final String dateOfBirth) {
        fillInputField(inputDateOfBirth, dateOfBirth);
    }

    public void submitNewAccountData() {
        click(buttonAddAccount);
    }
}