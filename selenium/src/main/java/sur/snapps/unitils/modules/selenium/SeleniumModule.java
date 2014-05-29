package sur.snapps.unitils.modules.selenium;

import com.saucelabs.common.SauceOnDemandAuthentication;
import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.unitils.core.Module;
import org.unitils.core.TestListener;
import org.unitils.util.ReflectionUtils;
import sur.snapps.unitils.modules.selenium.page.elements.Column;
import sur.snapps.unitils.modules.selenium.page.elements.WebPage;
import sur.snapps.unitils.modules.selenium.page.elements.WebTable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.unitils.util.ReflectionUtils.setFieldValue;

/**
 * User: SUR
 * Date: 4/05/14
 * Time: 15:53
 */
public class SeleniumModule implements Module {

    private static final Logger LOGGER = Logger.getLogger("SeleniumModule");

    private SeleniumConfiguration configuration;
    private WebDriver driver;
    private String testCase;
    private String testName;


    @Override
    public void init(Properties properties) {
        this.configuration = new SeleniumConfiguration(properties);
    }

    @Override
    public void afterInit() {
    }

    @Override
    public TestListener getTestListener() {
        return new SeleniumTestListener();
    }

    private void createAndInjectWebDriver(Object testObject) {
        Class<?> testClass = testObject.getClass();
        Set<Field> fields = ReflectionUtils.getAllFields(testClass);
        for (Field field : fields) {
            if (field.isAnnotationPresent(SeleniumWebDriver.class)) {
                if (field.getType().equals(WebDriver.class)) {
                    driver = createWebDriver();
                    System.out.println("driver = " + driver);
                    driver.get(configuration.baseUrl());
                    setFieldValue(testObject, field, driver);
                }
            }
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

    protected class SeleniumTestListener extends TestListener {

        private boolean active;

        @Override
        public void beforeTestClass(Class<?> testClass) {
            boolean annotationPresent = testClass.isAnnotationPresent(SeleniumTestCase.class);
            active = !configuration.annotationBased()
                    || annotationPresent;
            if (annotationPresent) {
                SeleniumTestCase seleniumTestCase = testClass.getAnnotation(SeleniumTestCase.class);
                testCase = isNullOrEmpty(seleniumTestCase.value()) ? testClass.getSimpleName() : seleniumTestCase.value();
            }

            super.beforeTestClass(testClass);
        }

        @Override
        public void beforeTestSetUp(Object testObject, Method testMethod) {
            if (active) {
                testName = testMethod.getName();
                createAndInjectWebDriver(testObject);
                createAndInjectWebPages(testObject);

                PageFactory.initElements(driver, testObject);
            }
            super.beforeTestSetUp(testObject, testMethod);
        }

        @Override
        public void afterTestTearDown(Object testObject, Method testMethod) {
            if (driver != null) {
                driver.quit();
            }
            super.afterTestTearDown(testObject, testMethod);
        }
    }
}
