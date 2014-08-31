package sur.snapps.jetta.selenium.module;

import sur.snapps.jetta.core.config.JettaConfiguration;
import sur.snapps.jetta.core.config.JettaProperty;
import sur.snapps.jetta.selenium.annotations.SeleniumWebDriverType;

/**
 * User: SUR
 * Date: 4/05/14
 * Time: 18:57
 */
@JettaConfiguration("sur.snapps.jetta.selenium")
public class SeleniumConfiguration {

    // TODO add current status of account, if this does not match the transaction amounts then that amount is accredited to something and any transaction added in that period has to be taken from there (perhaps not physically if it is a calculated amount)

    @JettaProperty(property = "annotation_only", required = false, defaultValue = "true")
    private boolean annotationBased;
    // TODO update code to allow null base url (should not cause test failure)
    @JettaProperty(property = "base_url", required = false)
    private String baseUrl;
    @JettaProperty(property = "driver", required = true)
    private SeleniumWebDriverType driverType;
    @JettaProperty(property = "sauce.activated", required = false, defaultValue = "false")
    private boolean sauceActivated;
    // TODO add attribute to specify to look also at system properties?
    @JettaProperty(property = "sauce.username", required = false)
    private String sauceUsername;
    @JettaProperty(property = "sauce.api_key", required = false)
    private String sauceApiKey;
    @JettaProperty(property = "screenshot_on_failure", required = false, defaultValue = "false")
    private boolean screenshotOnFailure;

    public boolean takeScreenshotOnFailure() {
        return screenshotOnFailure;
    }

    public boolean annotationBased() {
        return annotationBased;
    }

    public String baseUrl() {
        return baseUrl;
    }

    public boolean sauceActivated() {
        return sauceActivated;
    }

    public String sauceUsername() {
        return sauceUsername;
    }

    public String sauceApiKey() {
        return sauceApiKey;
    }

    public SeleniumWebDriverType driverType() {
        return driverType;
    }
}
