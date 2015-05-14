package sur.snapps.jetta.data.meta;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author sur
 * @since 07/03/2015
 */
public class JettaTable {

    private final int order;
    private final String tableName;
    private final List<JettaColumn> columns;
    private final List<Row> rows;

    public JettaTable(String tableName, int order) {
        this.tableName = tableName;
        this.columns = Lists.newLinkedList();
        this.rows = Lists.newArrayList();
        this.order = order;
    }

    public boolean contains(Row row) {
        return rows.contains(row);
    }

    public void add(Row row) {
        rows.add(row);
    }

    public void add(JettaColumn column) {
        columns.add(column);
    }

    public Integer order() {
        return order;
    }

    public String tableName() {
        return tableName;
    }

    // TODO only works with single primary key, adapt to support multiple and also @IdClass
    public JettaColumn primaryKey() {
        return FluentIterable.from(columns).filter(new Predicate<JettaColumn>() {

            @Override
            public boolean apply(JettaColumn column) {
                return column.isPrimaryKey();
            }
        }).first().orNull();
    }

    public List<Class<?>> dependencies() {
        Predicate<JettaColumn> IS_ENTITY = new Predicate<JettaColumn>() {
            @Override
            public boolean apply(JettaColumn column) {
                return column.isEntity();
            }
        };
        Function<JettaColumn, Class<?>> COLUMN_TYPES = new Function<JettaColumn, Class<?>>() {
            @Override
            public Class<?> apply(JettaColumn input) {
                return input.type();
            }
        };
        return FluentIterable
            .from(columns)
            .filter(IS_ENTITY)
            .transform(COLUMN_TYPES)
            .toList();
    }

    public List<JettaColumn> columns() {
        return columns;
    }

    public List<String> columnIds() {
        return FluentIterable.from(columns).transform(new Function<JettaColumn, String>() {
            @Override
            public String apply(JettaColumn column) {
                return column.name();
            }
        }).toList();
    }

    public List<Row> rows() {
        return rows;
    }
}
