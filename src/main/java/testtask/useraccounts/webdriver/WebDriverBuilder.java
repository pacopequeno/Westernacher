package testtask.useraccounts.webdriver;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebDriverBuilder {

    public WebDriverBuilder() {
    }

    public RemoteWebDriver createWebDriver() {

        DesiredCapabilities caps = DesiredCapabilities.chrome();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--lang=de");
        caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        caps.setCapability("acceptInsecureCerts", true);
        caps.setCapability("acceptSslCerts", true);

        ChromeDriverManager.getInstance().setup();
        return new ChromeDriver(caps);
    }
}