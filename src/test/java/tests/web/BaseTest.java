package tests.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.WebConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static tests.helpers.AllureAttachments.*;

public class BaseTest {
    public static WebConfig webConfig = ConfigFactory.create(WebConfig.class, System.getProperties());

    @BeforeAll
    static void setup() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.startMaximized = true;
        Configuration.browser = webConfig.getBrowserName();
        Configuration.browserVersion = webConfig.getBrowserVersion();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;

        if (webConfig.isRemote()) {
            String login = webConfig.selenideLogin();
            String password = webConfig.selenidePassword();
            String url = webConfig.selenideUrl();
            Configuration.remote = String.format("https://%s:%s@%s/wd/hub/", login, password, url);
        }
    }

    @AfterEach
    void afterEach() {
        addScreenshotAs("Last screenshot");
        addPageSource();
        addBrowserConsoleLogs();

        if (webConfig.selenideUrl() != null) {
            addVideo();
        }

        closeWebDriver();
    }
}
