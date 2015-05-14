package sur.snapps.jetta.core.logger;

import com.google.common.collect.Maps;
import org.unitils.util.PropertyUtils;
import sur.snapps.jetta.core.config.JettaProperties;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Activate logging for a specific module by adding this to config
 * sur.snapps.jetta.logging.module_name=LEVEL
 *
 * Default logging level for all modules:
 * sur.snapps.jetta.logging.default=LEVEL
 *
 * User: SUR
 * Date: 26/08/14
 * Time: 19:02
 */
public class JettaLogger {

    private static final Logger LOGGER = Logger.getLogger(JettaLogger.class.getSimpleName());

    private static Map<String, Level> modulesActivated;
    private static Level defaultLevel = Level.NONE;

    private JettaLogger() { }

    public static void warning(Class moduleClass, String message) {
        String moduleName = moduleClass.getSimpleName();
        if (moduleIsActivatedForLevel(Level.WARNING, moduleName)) {
            LOGGER.warning(moduleClass + " : " + message);
        }
    }

    public static void info(Class moduleClass, String message) {
        String moduleName = moduleClass.getSimpleName();
        if (moduleIsActivatedForLevel(Level.INFO, moduleName)) {
            LOGGER.info(moduleName + " : " + message);
        }
    }

    public static void debug(Class moduleClass, String message) {
        String moduleName = moduleClass.getSimpleName();
        if (moduleIsActivatedForLevel(Level.DEBUG, moduleName)) {
            LOGGER.info(moduleName + " : " + message);
        }
    }

    private static boolean moduleIsActivatedForLevel(Level level, String moduleName) {
        moduleName = moduleName.toLowerCase();
        if (modulesActivated().containsKey(moduleName)) {
            return modulesActivated().get(moduleName).shouldLogMessageOfLevel(level);
        }
        return defaultLevel.shouldLogMessageOfLevel(level);
    }

    private static Map<String, Level> modulesActivated() {
        if (modulesActivated == null) {
            initializeConfiguration();
        }
        return modulesActivated;
    }

    private static void initializeConfiguration() {
        modulesActivated = Maps.newHashMap();

        Properties properties = JettaProperties.get();
        defaultLevel = Level.valueOf(getDefaultLoggingLevel(properties));

        Enumeration<?> propertiesEnumeration = properties.propertyNames();
        while (propertiesEnumeration.hasMoreElements()) {
            String propertyName = (String) propertiesEnumeration.nextElement();
            String prefix = "sur.snapps.jetta.logging.";
            if (propertyName.startsWith(prefix)) {
                String moduleName = propertyName.substring(prefix.length());
                modulesActivated.put(moduleName,
                        Level.valueOf(PropertyUtils.getString(propertyName, getDefaultLoggingLevel(properties), properties)));
            }
        }
    }

    private static String getDefaultLoggingLevel(Properties properties) {
        return properties.getProperty("sur.snapps.jetta.logging.default", "NONE");
    }
}
