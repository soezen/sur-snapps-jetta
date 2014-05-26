package sur.snapps.unitils.modules.selenium;

import com.saucelabs.common.SauceOnDemandAuthentication;

import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.unitils.util.PropertyUtils.getBoolean;
import static org.unitils.util.PropertyUtils.getString;

/**
 * User: SUR
 * Date: 4/05/14
 * Time: 18:57
 */
public class SeleniumConfiguration {

    private static final String PREFIX = "sur.snapps.module.selenium.";
    private static final String ANNOTATION_BASED = PREFIX + "annotation_only";
    private static final String BASE_URL = PREFIX + "base_url";
    private static final String DRIVER_TYPE = PREFIX + "driver";
    private static final String SAUCE_ACTIVATED = PREFIX + "sauce.activated";
    private static final String SAUCE_USERNAME = PREFIX + "sauce.username";
    private static final String SAUCE_PASSWORD = PREFIX + "sauce.password";

    // TODO add current status of account, if this does not match the transaction amounts then that amount is accredited to something and any transaction added in that period has to be taken from there (perhaps not physically if it is a calculated amount)

    private boolean annotationBased;
    private String baseUrl;
    private SeleniumWebDriverType driverType;
    private boolean sauceActivated;
    private SauceOnDemandAuthentication sauceAuthentication;

    public SeleniumConfiguration(Properties properties) {
        annotationBased = getBoolean(ANNOTATION_BASED, true, properties);
        baseUrl = getString(BASE_URL, properties);
        driverType = SeleniumWebDriverType.valueOf(getString(DRIVER_TYPE, properties));
        sauceActivated = getBoolean(SAUCE_ACTIVATED, false, properties);

        if (sauceActivated) {
            String sauceUsername = getString(SAUCE_USERNAME, null, properties);
            String saucePassword = getString(SAUCE_PASSWORD, null, properties);
            checkNotNull(sauceUsername);
            checkNotNull(saucePassword);
            sauceAuthentication = new SauceOnDemandAuthentication(sauceUsername, saucePassword);
        }
    }

    public boolean annotationBased() {
        return annotationBased;
    }

    public String baseUrl() {
        return baseUrl;
    }

    public SeleniumWebDriverType driverType() {
        return driverType;
    }

    public boolean sauceActivated() {
        return sauceActivated;
    }

    public SauceOnDemandAuthentication sauceAuthentication() {
        return sauceAuthentication;
    }
}
