package sur.snapps.jetta.core.config;

import sur.snapps.jetta.core.logger.JettaLogger;

import java.io.IOException;
import java.util.Properties;

/**
 * User: SUR
 * Date: 26/08/14
 * Time: 20:33
 */
public class JettaProperties {

    private static Properties properties;

    private JettaProperties() { }

    public static Properties get() {
        // TODO make configuration property to specify whether properties need to be reloaded every time or not
        if (properties != null) {
            return properties;
        }

        properties = new Properties();
        loadProperties("jetta-config.properties");
        loadProperties("jetta-local-config.properties");
        return properties;
    }

    private static void loadProperties(String configFile) {
        try {
            properties.load(ClassLoader.getSystemResourceAsStream(configFile));
        } catch (NullPointerException | IOException e) {
            // NullPointerException when file not found because ClassLoader.getSystemResourceAsStream returns null in that case.
            JettaLogger.warning(JettaProperties.class, "Unable to load JettaConfiguration: " + configFile);
        }
    }
}
