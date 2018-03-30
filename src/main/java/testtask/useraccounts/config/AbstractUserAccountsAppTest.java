package testtask.useraccounts.config;

import org.junit.Assert;
import org.junit.Before;
import testtask.useraccounts.page.UserAccountsPage;

import java.util.*;

/**
 * Provides base functionality for testing User Accounts App.
 */
public abstract class AbstractUserAccountsAppTest extends AbstractSeleniumTest {

    private static final String APPLICATION_URL = "http://borisborisov.bg/user-accounts/";
    private UserAccountsPage userAccountsPage;
    private List<String> accountsToBeDeleted;

    @Before
    public final void setUpUserAccountsApp() {
        userAccountsPage = new UserAccountsPage(getWebDriver());
        accountsToBeDeleted = new ArrayList<>();
    }

    protected void requestUserAccountsApplication() {
        getWebDriver().get(APPLICATION_URL);
    }

    protected void searchUserAccountsTableForString(final String searchString) {
        userAccountsPage.searchUserAccountsTableForString(searchString);
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

    private void deleteAccount(final String accountIdentifier) {
        userAccountsPage.findElementWithCss(".delete [id = '" + accountIdentifier + "']").click();
        getWebDriver().switchTo().alert().accept();
    }

    protected void deleteAllNewAccounts() {
        for(String accountIdentifier : accountsToBeDeleted) {
            deleteAccount(accountIdentifier);
        }
    }

    /** #########################
     *  ### ASSERTION METHODS ###
     *  #########################*/

    protected void verifyUserAccountsPageIsDisplayed() {
        userAccountsPage.waitUntilUserAccountsPageIsDisplayed();
        Assert.assertTrue("Page title is not as expected!", userAccountsPage.getPageTitle().equals("User Accounts App"));
        Assert.assertTrue("Page headline is not as expected!", userAccountsPage.getHeadline().equals("User Accounts Table"));
    }

    protected void verifyAccountDataIsPresentAndCorrect(final String expectedFirstName, final String expectedLastName, final String expectedEmail, final String expectedDateOfBirth) {
        userAccountsPage.clearSearchField();
        Assert.assertTrue("Table seems to be empty but should not.", userAccountsPage.getCurrentUserAccountsTableRowCount() > 1);

        searchUserAccountsTableForString(expectedEmail);
        Assert.assertTrue("More than one occurrence of unique email address.", userAccountsPage.getCurrentUserAccountsTableRowCount() == 1);
        final String accountIdentifier = userAccountsPage.getTopAccountIdentifier();

        Assert.assertEquals("Field 'firstName' does not match the expected value.",
                userAccountsPage.findElementById("firstName_" + accountIdentifier).getText(), expectedFirstName);
        Assert.assertEquals("Field 'lastName' does not match the expected value.",
                userAccountsPage.findElementById("lastName_" + accountIdentifier).getText(), expectedLastName);
        Assert.assertEquals("Field 'email' does not match the expected value.",
                userAccountsPage.findElementById("email_" + accountIdentifier).getText(), expectedEmail);
        Assert.assertEquals("Field 'dateOfBirth' does not match the expected value.",
                userAccountsPage.findElementById("dateOfBirth_" + accountIdentifier).getText(), expectedDateOfBirth);

        accountsToBeDeleted.add(accountIdentifier);
        userAccountsPage.clearSearchField();
    }

    protected void verifyAccountIsNotPresent(final String emailAddress) {
        searchUserAccountsTableForString(emailAddress);
        Assert.assertTrue("There is an account present with email address '" + emailAddress + "' but should not.", userAccountsPage.isUserAccountsTableEmpty());
    }

    /** ######################
     *  ### HELPER METHODS ###
     *  ######################*/

    protected String createUniqueEmailAddress() {
        String randomEmailAddress = null;
        boolean notAlreadyInUse = false;
        while(!notAlreadyInUse) {
            randomEmailAddress = "frank" + new Random().nextInt(100) + "@grosse.com";
            userAccountsPage.searchUserAccountsTableForString(randomEmailAddress);
            notAlreadyInUse = userAccountsPage.isUserAccountsTableEmpty();
        }
        return randomEmailAddress;
    }
}