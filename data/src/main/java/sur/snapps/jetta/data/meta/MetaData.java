package sur.snapps.jetta.data.meta;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static org.unitils.util.ReflectionUtils.getAllFields;
import static org.unitils.util.ReflectionUtils.getFieldValue;
import static sur.snapps.jetta.data.meta.MetaDataExtractor.extractColumnName;
import static sur.snapps.jetta.data.meta.MetaDataExtractor.extractTableName;
import static sur.snapps.jetta.data.meta.MetaDataExtractor.isCollectionTable;
import static sur.snapps.jetta.data.meta.MetaDataExtractor.isEntity;
import static sur.snapps.jetta.data.util.Predicates.tableWithName;

/**
 * Contains the JPA metadata of an entity.
 *
 * @author sur
 * @since 28/02/2015
 */
public class MetaData {

    private List<JettaTable> tables = Lists.newArrayList();

    public void add(JettaTable table) {
        tables.add(table);
    }

    public JettaTable tableFor(String tableName) {
        return from(tables)
            .firstMatch(tableWithName(tableName))
            .orNull();
    }

    public List<JettaTable> orderedTables() {
        Comparator<JettaTable> BY_ORDER = new Comparator<JettaTable>() {
            @Override
            public int compare(JettaTable o1, JettaTable o2) {
                return o1.order().compareTo(o2.order());
            }
        };
        return FluentIterable.from(tables).toSortedList(BY_ORDER);
    }

    public void addRow(Object object) {
        Row row = createRow(object);
        JettaTable table = tableFor(extractTableName(object.getClass()));
        if (table.contains(row)) {
            return;
        }
        table.add(row);

        for (Field field : getAllFields(object.getClass())) {
            if (isEntity(field)) {
                addRow(getFieldValue(object, field));
            } else if (isCollectionTable(field)) {
                addCollectionRow(object, field);
            }
        }
    }

    private void addCollectionRow(Object object, Field field) {
        CollectionTable collectionTable = field.getAnnotation(CollectionTable.class);
        JettaTable table = tableFor(collectionTable.name());
        String joinColumn = collectionTable.joinColumns()[0].name();

        List<?> values = getFieldValue(object, field);
        Object primaryKeyValue = getPrimaryKeyValue(object);
        for (Object value : values) {
            Row row = new Row();
            row.add(joinColumn, primaryKeyValue);
            row.add(field.getAnnotation(Column.class).name(), value);
            table.add(row);
        }
    }

    private Row createRow(Object object) {
        Row row = new Row();

        for (Field field : getAllFields(object.getClass())) {
            Object fieldValue = getFieldValue(object, field);
            if (isEntity(field)) {
                row.add(extractColumnName(field), getPrimaryKeyValue(fieldValue));
            } else {
                row.add(extractColumnName(field), fieldValue);
            }
        }

        return row;
    }

    private Object getPrimaryKeyValue(Object entity) {
        for (Field field : getAllFields(entity.getClass())) {
            if (field.isAnnotationPresent(Id.class)) {
                return getFieldValue(entity, field);
            }
        }
        return null;
    }
}
