package sur.snapps.jetta.core.config;

import com.google.common.collect.Maps;
import org.unitils.util.PropertyUtils;
import org.unitils.util.ReflectionUtils;
import sur.snapps.jetta.core.logger.JettaLogger;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * User: SUR
 * Date: 26/08/14
 * Time: 20:47
 */
public class JettaConfigurations {

    private JettaConfigurations() { }

    private static final Properties properties = JettaProperties.get();

    private static Map<Class, Object> configurations = Maps.newHashMap();

    public static <T> T get(Class<T> clazz) {
        if (configurations.containsKey(clazz)) {
            return (T) configurations.get(clazz);
        }

        T instance = initialize(clazz);
        configurations.put(clazz, instance);
        return instance;
    }

    private static <T> T initialize(Class<T> clazz) {
        T instance = ReflectionUtils.createInstanceOfType(clazz, false);

        if (clazz.isAnnotationPresent(JettaConfiguration.class)) {
            initializeJettaProperties(instance);
        }

        return instance;
    }

    private static void initializeJettaProperties(Object configuration) {
        JettaConfiguration jettaConfiguration = configuration.getClass().getAnnotation(JettaConfiguration.class);
        String prefix = jettaConfiguration.value();

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


    private static void initializeJettaProperty(Object configuration, Field propertyField, String prefix) {
        JettaProperty jettaProperty = propertyField.getAnnotation(JettaProperty.class);
        String key = prefix + "." + jettaProperty.property();
        String defaultValue = jettaProperty.defaultValue();
        String propertyString = "";

        boolean required = jettaProperty.required();
        boolean present = PropertyUtils.containsProperty(key, properties);
        if (required && !present && defaultValue == null) {
            throw new IllegalStateException("missing required property: " + key);
        } else if (!present && defaultValue == null) {
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
        JettaLogger.debug(JettaConfiguration.class, "property loaded: " + key + " = " + propertyValue);
    }
}
