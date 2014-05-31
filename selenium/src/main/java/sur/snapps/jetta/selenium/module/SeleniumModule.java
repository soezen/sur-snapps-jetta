package sur.snapps.jetta.selenium.module;

import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.unitils.util.ReflectionUtils;
import sur.snapps.jetta.core.rules.JettaRuleModule;
import sur.snapps.jetta.selenium.annotations.SeleniumTestCase;
import sur.snapps.jetta.selenium.annotations.SeleniumWebDriver;
import sur.snapps.jetta.selenium.annotations.SeleniumWebDriverType;
import sur.snapps.jetta.selenium.elements.Column;
import sur.snapps.jetta.selenium.elements.Table;
import sur.snapps.jetta.selenium.elements.WebPage;
import sur.snapps.jetta.selenium.elements.WebTable;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * User: SUR
 * Date: 29/05/14
 * Time: 16:31
 */
public class SeleniumModule extends JettaRuleModule {

    private static final Logger LOGGER = Logger.getLogger("SeleniumModule");

    private SeleniumConfiguration configuration;
    private WebDriver driver;
    private String testCase;
    private String testName;

    public void init(Object target, String testName) {
        this.testName = testName;
        if (isActive(target)) {
            LOGGER.info("SeleniumModule ACTIVATED");
            createAndInjectWebDriver(target);
            createAndInjectWebPages(target);

            PageFactory.initElements(driver, target);
        }
    }

    private boolean isActive(Object target) {
        Class<?> testClass = target.getClass();
        boolean annotationPresent = testClass.isAnnotationPresent(SeleniumTestCase.class);
        boolean active = !configuration.annotationBased()
                || annotationPresent;
        if (active) {
            SeleniumTestCase annotation = testClass.getAnnotation(SeleniumTestCase.class);
            testCase =  isNullOrEmpty(annotation.value()) ? target.getClass().getSimpleName() : annotation.value();
        }
        return active;
    }

    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void createAndInjectWebDriver(Object target) {
        Class<?> testClass = target.getClass();
        Set<Field> fields = ReflectionUtils.getAllFields(testClass);
        for (Field field : fields) {
            if (field.isAnnotationPresent(SeleniumWebDriver.class)) {
                if (field.getType().equals(WebDriver.class)) {
                    driver = createWebDriver();
                    LOGGER.info("driver = " + driver);
                    LOGGER.info("base url " + configuration.baseUrl());
//                    driver.get(configuration.baseUrl());
                    driver.navigate().to(configuration.baseUrl());
                    ReflectionUtils.setFieldValue(target, field, driver);
                }
            }
        }
    }

    private WebDriver createWebDriver() {
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


    private WebDriver createSauceWebDriver() {
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
                    new URL("http://" + configuration.sauceUsername() + ":" + configuration.sauceApiKey() + "@ondemand.saucelabs.com:80/wd/hub"),
                    capabilities);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid url");
        }
    }

    // TODO what about cyclic dependencies
    private void createAndInjectWebPages(Object testObject) {
        Set<Field> webPages = ReflectionUtils.getAllFields(testObject.getClass());
        for (Field field : webPages) {
            if (field.isAnnotationPresent(WebPage.class)) {
                Object webPage = ReflectionUtils.createInstanceOfType(field.getType(), true);
                PageFactory.initElements(driver, webPage);
                createAndInjectWebPages(webPage);
                ReflectionUtils.setFieldValue(testObject, field, webPage);
                LOGGER.info(testObject.getClass().getSimpleName() + "." + field.getName() + " = " + webPage);
            } else if (field.isAnnotationPresent(WebTable.class)) {
                WebTable webTable = field.getAnnotation(WebTable.class);
                String id = webTable.id();
                Object table = ReflectionUtils.createInstanceOfType(Table.class, true,
                        new Class[] { WebDriver.class, String.class},
                        new Object[] { driver, id});
                ReflectionUtils.setFieldValue(testObject, field, table);
                LOGGER.info(testObject.getClass().getSimpleName() + "." + field.getName() + " = " + table);

                Field columnIndicesField = ReflectionUtils.getFieldWithName(Table.class, "columns", false);
                Map<String, Column> columnIndices = ReflectionUtils.getFieldValue(table, columnIndicesField);
                for (Column column : webTable.columns()) {
                    columnIndices.put(column.name(), column);
                }
            }
        }
    }
}
