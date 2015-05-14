package sur.snapps.jetta.data.meta;


import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.lang.reflect.Field;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.reflect.Modifier.isFinal;
import static org.unitils.util.ReflectionUtils.getAllFields;

/**
 * @author sur
 * @since 28/02/2015
 */
public class MetaDataExtractor {

    public static MetaData extract(Class<?> dataClass) {
        MetaData metaData = new MetaData();
        extractTable(metaData, dataClass, 100);
        return metaData;
    }

    private static JettaTable extractTable(MetaData metaData, Class<?> dataClass, Integer order) {
        String tableName = extractTableName(dataClass);
        JettaTable table = new JettaTable(tableName, order);
        for (Field field : getAllFields(dataClass)) {
            if (!isFinal(field.getModifiers())) {
                if (isEntity(field)) {
                    extractTable(metaData, field.getType(), order - 1);
                    table.add(extractColumn(field));
                } else if (isCollectionTable(field)) {
                    CollectionTable collectionTableAnnotation = field.getAnnotation(CollectionTable.class);
                    JettaTable collectionTable = new JettaTable(collectionTableAnnotation.name(), order + 1);
                    collectionTable.add(new JettaColumn(String.class, collectionTableAnnotation.joinColumns()[0].name(), true));
                    collectionTable.add(new JettaColumn(String.class, field.getAnnotation(Column.class).name(), false));
                    metaData.add(collectionTable);
                } else {
                    table.add(extractColumn(field));
                }
            }
        }
        metaData.add(table);
        return table;
    }

    private static JettaColumn extractColumn(Field field) {
        String name = extractColumnName(field);
        boolean primaryKey = extractPrimaryKey(field);
        return new JettaColumn(field.getType(), name, primaryKey);
    }

    private static boolean extractPrimaryKey(Field field) {
        return field.isAnnotationPresent(Id.class);
    }

    public static String extractColumnName(Field field) {
        if (field.isAnnotationPresent(JoinColumn.class)) {
            return field.getAnnotation(JoinColumn.class).name();
        }
        return LOWER_CAMEL.to(LOWER_UNDERSCORE, field.getName());
    }

    public static String extractTableName(Class<?> dataClass) {
        Table tableAnnotation = dataClass.getAnnotation(Table.class);
        if (isNullOrEmpty(tableAnnotation.name())) {
            return dataClass.getSimpleName().toUpperCase();
        }
        return tableAnnotation.name();
    }

    public static boolean isEntity(Field field) {
        return field.getType().getAnnotation(Entity.class) != null;
    }

    public static boolean isCollectionTable(Field field) {
        return field.getAnnotation(CollectionTable.class) != null;
    }
}
