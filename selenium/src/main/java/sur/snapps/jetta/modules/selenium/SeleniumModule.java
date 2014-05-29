package sur.snapps.jetta.modules.selenium;

import com.saucelabs.common.SauceOnDemandAuthentication;
import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.unitils.util.ReflectionUtils;
import sur.snapps.jetta.modules.selenium.annotations.SeleniumWebDriver;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * User: SUR
 * Date: 29/05/14
 * Time: 16:31
 */
public class SeleniumModule {

    private static SeleniumConfiguration configuration;
    private static WebDriver driver;
    private static String testCase;
    private static String testName;

    public static void init(Object target) {
        configuration = new SeleniumConfiguration();
        createAndInjectWebDriver(target);
    }

    public static void quit() {
        if (driver != null) {
            driver.quit();
        }
    }

    private static void createAndInjectWebDriver(Object target) {
        Class<?> testClass = target.getClass();
        Set<Field> fields = ReflectionUtils.getAllFields(testClass);
        for (Field field : fields) {
            if (field.isAnnotationPresent(SeleniumWebDriver.class)) {
                if (field.getType().equals(WebDriver.class)) {
                    driver = createWebDriver();
                    driver.get(configuration.baseUrl());
                    ReflectionUtils.setFieldValue(target, field, driver);
                }
            }
        }
    }

    private static WebDriver createWebDriver() {
        if (configuration.sauceActivated()) {
            return createSauceWebDriver();
        }
        SeleniumWebDriverType type = configuration.driverType();
        switch (type) {
            case FIREFOX:
                return new FirefoxDriver(DesiredCapabilities.firefox());
            default:
                throw new NotImplementedException("TODO");
        }
    }


    private static WebDriver createSauceWebDriver() {
        SauceOnDemandAuthentication authentication = configuration.sauceAuthentication();
        // TODO get driver type from configuration
        DesiredCapabilities capabilities;
        switch (configuration.driverType()) {
            case CHROME:
                capabilities = DesiredCapabilities.chrome();
                // TODO find correct version, what if version is not set?
                break;
            case FIREFOX:
                capabilities = DesiredCapabilities.firefox();
                capabilities.setCapability("version", "28");
                break;
            case INTERNET_EXPLORER:
                capabilities = DesiredCapabilities.internetExplorer();
                break;
            default:
                throw new IllegalStateException();
        }
        // TODO make this unitils property
        capabilities.setCapability("platform", Platform.WIN8);
        capabilities.setCapability("name", testCase + " : " + testName);
//        capabilities.setCapability("tags", testCase);

        try {
            return new RemoteWebDriver(
                    new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"),
                    capabilities);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid url");
        }
    }
}
