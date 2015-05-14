package sur.snapps.jetta.dummy;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sur.snapps.jetta.dummy.annotation.Dummies;
import sur.snapps.jetta.dummy.annotation.DummyConfig;
import sur.snapps.jetta.dummy.xml.XmlDummy;
import sur.snapps.jetta.dummy.xml.element.XmlDummyComplexValue;
import sur.snapps.jetta.dummy.xml.element.XmlDummyElement;
import sur.snapps.jetta.dummy.xml.element.XmlDummyList;
import sur.snapps.jetta.dummy.xml.element.XmlDummyReference;
import sur.snapps.jetta.dummy.xml.element.XmlDummyValue;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.unitils.util.ReflectionUtils.createInstanceOfType;
import static org.unitils.util.ReflectionUtils.getAllFields;
import static org.unitils.util.ReflectionUtils.setFieldValue;
import static sur.snapps.jetta.dummy.xml.XmlDummyDocument.getNodeList;

/**
 * Collection of dummies that are present in the dummy file.
 *
 * Created by sur on 19/02/2015.
 */
public final class DummyCollection {

    private static Map<Dummies, DummyFindersByConfig> dummyFindersByConfig = Maps.newHashMap();

    private DummyCollection() {}

    public static DummyFindersByConfig with(Dummies configuration) {
        if (!dummyFindersByConfig.containsKey(configuration)) {
            dummyFindersByConfig.put(configuration, new DummyFindersByConfig(configuration));
        }
        return dummyFindersByConfig.get(configuration);
    }

    public static class DummyFindersByConfig {

        private Dummies configuration;
        private Map<DummyConfig, DummyFinder<?>> dummyFinders = Maps.newHashMap();

        public DummyFindersByConfig(Dummies configuration) {
            this.configuration = configuration;
        }

        public <D> DummyFinder<D> find(Class<D> dummyClass) {
            checkNotNull(dummyClass, "dummyClass is required");
            DummyConfig dummyConfig = configFor(dummyClass);
            checkNotNull(dummyConfig, "dummyConfig for " + dummyClass + " is required");
            if (dummyFinders.containsKey(dummyConfig)) {
                return (DummyFinder<D>) dummyFinders.get(dummyConfig);
            }
            DummyFinder<D> dummyFinder = createDummyFinder(dummyClass, dummyConfig);
            dummyFinders.put(dummyConfig, dummyFinder);
            return dummyFinder;
        }

        private <D> DummyFinder<D> createDummyFinder(Class<D> dummyClass, DummyConfig dummyConfig) {
            DummyFinder<D> dummyFinder = new DummyFinder<>(dummyConfig.identifier());
            dummiesFor(dummyClass, dummyConfig.path(), dummyFinder);
            return dummyFinder;
        }

        private DummyConfig configFor(Class<?> clazz) {
            for (DummyConfig config : configuration.value()) {
                if (config.type().equals(clazz)) {
                    return config;
                }
            }
            return null;
        }

        private <D> void dummiesFor(Class<D> dummyClass, String path, DummyFinder<D> dummyFinder) {
            NodeList dummyNodes = getNodeList(path);
            for (int i = 0; i < dummyNodes.getLength(); i++) {
                Node dummyNode = dummyNodes.item(i);
                D dummy = createDummy(dummyClass, new XmlDummy(dummyNode), dummyFinder);
                dummyFinder.add(dummy);
            }
        }

        private <D> D createDummy(Class<D> dummyClass, XmlDummy xmlDummy, DummyFinder<D> dummyFinder) {
            D instance = createInstanceOfType(dummyClass, true);

            Set<Field> fields = getAllFields(dummyClass);
            for (Field field : fields) {
                String fieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());

                Object value = null;
                XmlDummyElement xmlDummyElement = xmlDummy.get(fieldName);
                if (field.getType().equals(dummyClass)) {
                    value = dummyFinder.where("").is("");
                } else if (xmlDummyElement instanceof XmlDummyList
                        && getGenericClassOfList(field.getGenericType()).equals(dummyClass)) {
                    XmlDummyList xmlDummyList = (XmlDummyList) xmlDummyElement;
                    List<D> values = Lists.newArrayList();
                    for (XmlDummyElement xmlDummyChild : xmlDummyList.getValues()) {
                        if (xmlDummyChild instanceof XmlDummyReference) {
                            XmlDummyReference xmlDummyReference = (XmlDummyReference) xmlDummyChild;
                            values.add(dummyFinder.where(xmlDummyReference.getReferenceIdentifier()).is(xmlDummyReference.getReferenceIdentifierValue()));
                        }
                    }
                    value = values;
                } else {
                    value = createValue(field.getType(), xmlDummyElement, field.getGenericType());
                }
                if (value != null) {
                    setFieldValue(instance, field, value);
                }
            }
            return instance;
        }

        private Object createValue(Class<?> elementClass, XmlDummyElement element, Type genericType) {
            if (element instanceof XmlDummyValue) {
                XmlDummyValue xmlDummyValue = (XmlDummyValue) element;
                return xmlDummyValue.getValue(elementClass);
            }
            if (element instanceof XmlDummyReference) {
                XmlDummyReference xmlDummyReference = (XmlDummyReference) element;
                return this.find(elementClass)
                    .where(xmlDummyReference.getReferenceIdentifier())
                    .is(xmlDummyReference.getReferenceIdentifierValue());
            }
            if (element instanceof XmlDummyComplexValue) {
                XmlDummyComplexValue xmlDummyComplexValue = (XmlDummyComplexValue) element;
                return createDummy(elementClass, xmlDummyComplexValue.getValue(), null);
            }
            if (element instanceof XmlDummyList) {
                XmlDummyList xmlDummyList = (XmlDummyList) element;
                List<Object> values = Lists.newArrayList();
                for (XmlDummyElement xmlDummyChild : xmlDummyList.getValues()) {
                    values.add(createValue(getGenericClassOfList(genericType), xmlDummyChild, null));
                }
                return values;
            }
            return null;
        }

        private Class<?> getGenericClassOfList(Type genericType) {
            return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
        }
    }
}
