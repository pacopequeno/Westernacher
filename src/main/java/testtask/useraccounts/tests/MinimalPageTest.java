package testtask.useraccounts.tests;

import org.junit.Test;
import testtask.useraccounts.config.AbstractUserAccountsAppTest;

public class MinimalPageTest extends AbstractUserAccountsAppTest {

    @Test
    public void checkHappyPathOnUserAccountsPage() {

        final String testAccountIdentifier;

        // given
        final String firstName = "Frank";
        final String lastName = "Grosse";
        String emailAddress;
        final String dateOfBirth = "17/05/1981 11:11";
        // when
        requestUserAccountsApplication();
        // then
        verifyUserAccountsPageIsDisplayed();

        // given
        emailAddress = createUniqueEmailAddress();
        // when
        createNewAccount(firstName, lastName, emailAddress, dateOfBirth);
        // then
        testAccountIdentifier = verifyAccountDataIsPresentAndCorrect(firstName, lastName, emailAddress, dateOfBirth);

        // when
        deleteAccount(testAccountIdentifier);
        // then
        verifyAccountIsNotPresent(emailAddress);
    }
}