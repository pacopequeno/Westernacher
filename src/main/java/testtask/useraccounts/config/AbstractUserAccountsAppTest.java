package testtask.useraccounts.config;

import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.WebElement;
import testtask.useraccounts.page.UserAccountsPage;

import java.util.List;
import java.util.Random;

/**
 * Provides base functionality for testing User Accounts App.
 */
public abstract class AbstractUserAccountsAppTest extends AbstractSeleniumTest {

    protected UserAccountsPage userAccountsPage;
    private static final String APPLICATION_URL = "http://borisborisov.bg/user-accounts/";

    @Before
    public final void setUpUserAccountsApp() {
        // initialize page object to be tested
        userAccountsPage = new UserAccountsPage(getWebDriver());
    }

    protected void requestUserAccountsApplication() {
        getWebDriver().get(APPLICATION_URL);
    }

    protected void searchTableForString(final String searchString) {
        userAccountsPage.searchTableForString(searchString);
    }

    protected void createNewAccount(final String firstName, final String lastName, final String email, final String dateOfBirth) {
        insertNewAccountData(firstName, lastName, email, dateOfBirth);
        submitNewAccountData();
    }

    private void insertNewAccountData(final String firstName, final String lastName, final String email, final String dateOfBirth) {
        userAccountsPage.setFirstName(firstName);
        userAccountsPage.setLastName(lastName);
        userAccountsPage.setEmail(email);
        userAccountsPage.setDateOfBirth(dateOfBirth);
    }

    private void submitNewAccountData() {
        userAccountsPage.submitNewAccountData();
    }

    protected void deleteAccount(final String accountIdentifier) {
        findElementWithCss(".delete [id = '" + accountIdentifier + "']").click();
        getWebDriver().switchTo().alert().accept();
    }


    /** ######################
     *  ### HELPER METHODS ###
     *  ######################*/

    protected String createUniqueEmailAddress() {
        String randomEmailAddress = null;
        boolean notAlreadyInUse = false;
        while(!notAlreadyInUse) {
            randomEmailAddress = "frank" + new Random().nextInt(100) + "@grosse.com";
            userAccountsPage.searchTableForString(randomEmailAddress);
            notAlreadyInUse = isTableEmpty();
        }
        return randomEmailAddress;
    }

    private boolean isTableEmpty() {
        return getAllTableRows().get(0).getText().equals("User Accounts Table Empty");
    }

    private List<WebElement> getAllTableRows() {
        return findElementsWithCss("[role = 'row']");
    }

    private int getCurrentTableRowCount() {
        return getAllTableRows().size() - 2; // because of two header rows
    }

    private String getAccountIdentifier() {
        return getAllTableRows().get(2).getAttribute("id"); // first two rows represent table header
    }


    /** #########################
     *  ### ASSERTION METHODS ###
     *  #########################*/

    protected void verifyUserAccountsPageIsDisplayed() {
        userAccountsPage.waitUntilUserAccountsPageIsDisplayed();
        Assert.assertTrue("Page title is not as expected!", userAccountsPage.getPageTitle().equals("User Accounts App"));
        Assert.assertTrue("Page headline is not as expected!", userAccountsPage.getHeadline().equals("User Accounts Table"));
    }

    protected String verifyAccountDataIsPresentAndCorrect(final String expectedFirstName, final String expectedLastName, final String expectedEmail, final String expectedDateOfBirth) {
        userAccountsPage.clearSearchField();
        Assert.assertTrue("Table seems to be empty but should not.", getCurrentTableRowCount() > 1);

        searchTableForString(expectedEmail);
        Assert.assertTrue("More than one occurrence of unique email address.", getCurrentTableRowCount() == 1);
        final String accountIdentifier = getAccountIdentifier();

        Assert.assertEquals("Field 'firstName' does not match the expected value.", findElementById("firstName_" + accountIdentifier).getText(), expectedFirstName);
        Assert.assertEquals("Field 'lastName' does not match the expected value.", findElementById("lastName_" + accountIdentifier).getText(), expectedLastName);
        Assert.assertEquals("Field 'email' does not match the expected value.", findElementById("email_" + accountIdentifier).getText(), expectedEmail);
        Assert.assertEquals("Field 'dateOfBirth' does not match the expected value.", findElementById("dateOfBirth_" + accountIdentifier).getText(), expectedDateOfBirth);

        return accountIdentifier;
    }

    protected void verifyAccountIsNotPresent(final String emailAddress) {
        searchTableForString(emailAddress);
        Assert.assertTrue("There is an account present with email address '" + emailAddress + "' but should not.", isTableEmpty());
    }
}