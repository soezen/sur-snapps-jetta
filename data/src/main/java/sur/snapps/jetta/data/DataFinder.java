package sur.snapps.jetta.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.w3c.dom.Node;
import sur.snapps.jetta.data.meta.JettaTable;
import sur.snapps.jetta.data.xml.XmlData;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.unitils.util.ReflectionUtils.getFieldValue;
import static org.unitils.util.ReflectionUtils.getFieldWithName;
import static sur.snapps.jetta.data.DataCreator.createDataObject;
import static sur.snapps.jetta.data.meta.MetaDataExtractor.extract;
import static sur.snapps.jetta.data.meta.MetaDataExtractor.extractTableName;
import static sur.snapps.jetta.data.xml.XmlDocument.getNodes;

/**
 * @author sur
 * @since 20/02/2015
 */
public class DataFinder<D> {

    private Class<D> dataClass;
    private List<D> dataObjects;
    private JettaTable metaData;
    private boolean filterTransient;

    public DataFinder(Class<D> dataClass, boolean filterTransient) {
        this.dataClass = dataClass;
        this.filterTransient = filterTransient;
        metaData = extract(dataClass).tableFor(extractTableName(dataClass));
        dataObjects = Lists.newArrayList();
        initializeDataObjects();
    }

    public DataFinder(Class<D> dataClass) {
        this(dataClass, false);
    }

    private void initializeDataObjects() {
        final String path = "//data/" + metaData.tableName().toLowerCase() + (filterTransient ? "/record[not(@transient='true')]" : "/record");
        List<Node> nodes = getNodes(path);
        for (Node dummyNode : nodes) {
            D dataObject = createDataObject(dataClass, new XmlData(dummyNode), this);
            dataObjects.add(dataObject);
        }
    }

    public DataByIdentifier where(String identifier) {
        return new DataByIdentifier(identifier);
    }

    public List<D> all() {
        return dataObjects;
    }

    /**
     * Container with map of all data objects by unique identifier.
     * Given identifier has to be unique over all data objects.
     */
    public class DataByIdentifier {

        private Map<String, List<D>> dataByIdentifier = Maps.newHashMap();

        public DataByIdentifier(String identifier) {
            String actualIdentifier = isNullOrEmpty(identifier) ? metaData.primaryKey().name() : identifier;
            String attribute = null;
            if (actualIdentifier.contains("@")) {
                attribute = actualIdentifier.substring(actualIdentifier.indexOf("@") + 1);
                actualIdentifier = actualIdentifier.substring(0, actualIdentifier.indexOf("@"));
            }
            for (D dataObject : dataObjects) {
                Field field = getFieldWithName(dataObject.getClass(), actualIdentifier, false);
                if (field == null) {
                    throw new IllegalArgumentException("Invalid identifier '" + actualIdentifier + "', no such field found in " + dataObject.getClass());
                }
                String value;
                Object fieldValue = getFieldValue(dataObject, field);
                if (attribute != null) {
                    value = getFieldValue(fieldValue, getFieldWithName(fieldValue.getClass(), attribute, false));
                } else {
                    value = (String) fieldValue;
                }
                if (!dataByIdentifier.containsKey(value)) {
                    dataByIdentifier.put(value, Lists.newArrayList(dataObject));
                } else {
                    dataByIdentifier.get(value).add(dataObject);
                }
            }
        }

        public ValueHolder<D> is(String identifierValue) {
            return new ValueHolder<>(dataByIdentifier.get(identifierValue));
        }
    }

    public class ValueHolder<T> {
        private List<T> value;

        public ValueHolder(List<T> value) {
            this.value = (value == null ? Lists.<T>newArrayList() : value);
        }

        public T singleResult() {
            return value.isEmpty() ? null : value.get(0);
        }

        public List<T> allResults() {
            return value;
        }
    }
}
