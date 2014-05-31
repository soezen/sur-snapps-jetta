package sur.snapps.jetta.core;

import org.unitils.util.PropertyUtils;
import org.unitils.util.ReflectionUtils;
import sur.snapps.jetta.core.config.JettaConfiguration;
import sur.snapps.jetta.core.config.JettaProperty;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 13:42
 */
public class JettaModule {


    protected static final Logger LOGGER = Logger.getLogger("JettaRuleModule");

    private Properties properties;

    public JettaModule() {
        properties = new Properties();
        loadProperties("jetta-config.properties");
        loadProperties("jetta-local-config.properties");

        // look for jetta configuration and initialize values
        Set<Field> fields = ReflectionUtils.getAllFields(this.getClass());
        for (Field field : fields) {
            if (field.getType().isAnnotationPresent(JettaConfiguration.class)) {
                initializeJettaConfiguration(field);
            }
        }
    }

    private void loadProperties(String configFile) {
        try {
            properties.load(ClassLoader.getSystemResourceAsStream(configFile));
        } catch (IOException e) {
            LOGGER.warning("Unable to load JettaConfiguration: " + configFile);
        }
    }

    private void initializeJettaConfiguration(Field configurationField) {
        JettaConfiguration jettaConfiguration = configurationField.getType().getAnnotation(JettaConfiguration.class);
        String prefix = jettaConfiguration.value();
        Object configuration = ReflectionUtils.createInstanceOfType(configurationField.getType(), false);
        ReflectionUtils.setFieldValue(this, configurationField, configuration);

        Set<Field> fields = ReflectionUtils.getAllFields(configuration.getClass());
        for (Field field : fields) {
            if (field.isAnnotationPresent(JettaProperty.class)
                    // TODO also allow integers and such
                    && (field.getType().equals(String.class)
                    || field.getType().equals(Boolean.class))
                    || field.getType().equals(boolean.class)
                    || field.getType().isEnum()) {
                initializeJettaProperty(configuration, field, prefix);
            }
        }
    }

    private void initializeJettaProperty(Object configuration, Field propertyField, String prefix) {
        JettaProperty jettaProperty = propertyField.getAnnotation(JettaProperty.class);
        String key = prefix + "." + jettaProperty.property();
        String defaultValue = jettaProperty.defaultValue();
        String propertyString = "";

        boolean required = jettaProperty.required();
        boolean present = PropertyUtils.containsProperty(key, properties);
        if (required && !present) {
            throw new IllegalStateException("missing required property: " + key);
        } else if (!present) {
            return;
        }

        if (required) {
            propertyString = PropertyUtils.getString(key, properties);
        } else {
            propertyString = PropertyUtils.getString(key, defaultValue, properties);
        }

        Object propertyValue = null;
        if (propertyField.getType().equals(Boolean.class)
                || propertyField.getType().equals(boolean.class)) {
            propertyValue = Boolean.parseBoolean(propertyString);
        } else if (propertyField.getType().equals(String.class)) {
            propertyValue = propertyString;
        } else if (propertyField.getType().equals(Integer.class)) {
            propertyValue = Integer.valueOf(propertyString);
        } else if (propertyField.getType().isEnum()) {
            propertyValue = Enum.valueOf((Class<Enum>) propertyField.getType(), propertyString);
        }

        ReflectionUtils.setFieldValue(configuration, propertyField, propertyValue);
        LOGGER.info("config: " + key + " = " + propertyValue);
    }
}
