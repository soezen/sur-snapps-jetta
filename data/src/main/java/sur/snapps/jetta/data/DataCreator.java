package sur.snapps.jetta.data;

import com.google.common.collect.Lists;
import sur.snapps.jetta.core.logger.JettaLogger;
import sur.snapps.jetta.data.module.DataModule;
import sur.snapps.jetta.data.xml.XmlData;
import sur.snapps.jetta.data.xml.element.XmlComplexValue;
import sur.snapps.jetta.data.xml.element.XmlElement;
import sur.snapps.jetta.data.xml.element.XmlList;
import sur.snapps.jetta.data.xml.element.XmlReference;
import sur.snapps.jetta.data.xml.element.XmlValue;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static org.unitils.util.ReflectionUtils.createInstanceOfType;
import static org.unitils.util.ReflectionUtils.getAllFields;
import static org.unitils.util.ReflectionUtils.setFieldValue;

/**
 * @author sur
 * @since 02/03/2015
 */
public class DataCreator {

    public static <D> D createDataObject(Class<D> dataClass, XmlData xmlData, DataFinder<D> dataFinder) {
        JettaLogger.debug(DataModule.class, "CREATING DATA OBJECT : " + dataClass.getSimpleName());
        D instance = createInstanceOfType(dataClass, true);

        Set<Field> fields = getAllFields(dataClass);
        for (Field field : fields) {
            String fieldName = LOWER_CAMEL.to(LOWER_UNDERSCORE, field.getName());

            Object value;
            XmlElement xmlElement = xmlData.get(fieldName);
            if (field.getType().equals(dataClass)) {
                // TODO fix this
                value = dataFinder.where("").is("");
            } else if (xmlElement instanceof XmlList
                && getGenericClassOfList(field.getGenericType()).equals(dataClass)) {
                XmlList xmlDummyList = (XmlList) xmlElement;
                List<D> values = Lists.newArrayList();
                for (XmlElement xmlDummyChild : xmlDummyList.getValues()) {
                    if (xmlDummyChild instanceof XmlReference) {
                        XmlReference xmlDummyReference = (XmlReference) xmlDummyChild;
                        values.addAll(dataFinder
                            .where(xmlDummyReference.getReferenceIdentifier())
                            .is(xmlDummyReference.getReferenceIdentifierValue())
                            .allResults());
                    }
                }
                value = values;
            } else {
                value = createValue(field.getType(), xmlElement, field.getGenericType());
            }
            if (value != null) {
                setFieldValue(instance, field, value);
            }
        }
        return instance;
    }

    private static Object createValue(Class<?> elementClass, XmlElement element, Type genericType) {
        if (element instanceof XmlValue) {
            XmlValue xmlDummyValue = (XmlValue) element;
            return xmlDummyValue.getValue(elementClass);
        }
        if (element instanceof XmlReference) {
            XmlReference xmlDummyReference = (XmlReference) element;
            return DataCollection.find(elementClass)
                .where(xmlDummyReference.getReferenceIdentifier())
                .is(xmlDummyReference.getReferenceIdentifierValue())
                .singleResult();
        }
        if (element instanceof XmlComplexValue) {
            XmlComplexValue xmlDummyComplexValue = (XmlComplexValue) element;
            return createDataObject(elementClass, xmlDummyComplexValue.getValue(), null);
        }
        if (element instanceof XmlList) {
            XmlList xmlDummyList = (XmlList) element;
            List<Object> values = Lists.newArrayList();
            for (XmlElement xmlDummyChild : xmlDummyList.getValues()) {
                values.add(createValue(getGenericClassOfList(genericType), xmlDummyChild, null));
            }
            return values;
        }
        return null;
    }

    private static Class<?> getGenericClassOfList(Type genericType) {
        return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }

}
