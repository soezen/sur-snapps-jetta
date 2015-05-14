package sur.snapps.jetta.data.meta;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author sur
 * @since 07/03/2015
 */
public class Row {

    private Map<String, Object> values = Maps.newHashMap();

    public void add(String identifier, Object value) {
        values.put(identifier, value);
    }
    
    public Object[] values(List<String> orderedColumns) {
        Object[] objects = new Object[orderedColumns.size()];

        for (String columnId : orderedColumns) {
            objects[orderedColumns.indexOf(columnId)] = values.get(columnId);
        }
        return objects;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Row
            && (Objects.equals(values, ((Row) obj).values));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(values);
    }
}
