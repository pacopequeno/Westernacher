package testtask.useraccounts.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

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

    public boolean isUserAccountsTableEmpty() {
        return getAllUserAccountsTableRows().get(0).getText().equals("User Accounts Table Empty");
    }

    public void searchUserAccountsTableForString(final String searchString) {
        fillInputField(inputSearch, searchString);
    }

    public int getCurrentUserAccountsTableRowCount() {
        return getAllUserAccountsTableRows().size() - 2; // because of two header rows
    }

    private List<WebElement> getAllUserAccountsTableRows() {
        return findElementsWithCss("[role = 'row']");
    }

    public String getTopAccountIdentifier() {
        return getAllUserAccountsTableRows().get(2).getAttribute("id"); // first two rows represent table header
    }

    public void clearSearchField() {
        clearInputField(inputSearch);
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