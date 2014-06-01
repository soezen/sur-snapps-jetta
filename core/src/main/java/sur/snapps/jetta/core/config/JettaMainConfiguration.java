package sur.snapps.jetta.core.config;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 23:36
 */
public class JettaMainConfiguration {

    @JettaProperty(property = "logging", required = false, defaultValue = "false")
    private boolean logging;

    public boolean logging() {
        return logging;
    }
}
