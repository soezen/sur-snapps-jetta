package sur.snapps.jetta.data.dbunit;

import com.google.common.collect.Lists;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.DefaultTableMetaData;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.DataType;
import sur.snapps.jetta.core.logger.JettaLogger;
import sur.snapps.jetta.data.DataCollection;
import sur.snapps.jetta.data.DataSet;
import sur.snapps.jetta.data.meta.JettaColumn;
import sur.snapps.jetta.data.meta.JettaTable;
import sur.snapps.jetta.data.meta.MetaData;
import sur.snapps.jetta.data.meta.Row;
import sur.snapps.jetta.data.module.DataModule;

import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Arrays.copyOf;
import static sur.snapps.jetta.data.meta.MetaDataExtractor.extract;

/**
 * @author sur
 * @since 06/03/2015
 */
public class JettaDataSet extends DefaultDataSet {

    public JettaDataSet(DataSet data) throws Exception {
        MetaData metaData = extract(data.type());
        List<Object> objects = Lists.newArrayList();
        if (isNullOrEmpty(data.selector())) {
            objects.addAll(DataCollection.findPersistentObjects(data.type()).all());
        } else {
            for (String value : data.value()) {
                objects.addAll(DataCollection.findPersistentObjects(data.type()).where(data.selector()).is(value).allResults());
            }
        }

        for (Object object : objects) {
            metaData.addRow(object);
        }

        for (JettaTable table : metaData.orderedTables()) {
            addTable(transform(table));
        }
    }

    private ITable transform(JettaTable table) throws Exception {
        JettaLogger.debug(DataModule.class, "DATASET: ADD TABLE " + table.tableName());
        String tableName = table.tableName();
        Column[][] columns = transform(table.columns());
        DefaultTable defaultTable = new DefaultTable(new DefaultTableMetaData(tableName, columns[0], columns[1]));

        List<String> orderedColumns = table.columnIds();
        for (Row row : table.rows()) {
            defaultTable.addRow(row.values(orderedColumns));
        }

        return defaultTable;
    }

    private Column[][] transform(List<JettaColumn> columns) {
        Column[] dbunitColumns = new Column[columns.size()];
        Column[] primaryKeys = new Column[0];

        for (int i = 0; i < columns.size(); i++) {
            JettaColumn column = columns.get(i);
            dbunitColumns[i] = transform(column);
            if (column.isPrimaryKey()) {
                primaryKeys = copyOf(primaryKeys, primaryKeys.length + 1);
                primaryKeys[primaryKeys.length - 1] = dbunitColumns[i];
            }
        }
        return new Column[][] {
            dbunitColumns,
            primaryKeys
        };
    }

    private Column transform(JettaColumn column) {
        return new Column(column.name(), transform(column.type()));
    }

    private DataType transform(Class<?> type) {
        return DataType.VARCHAR;
    }
}
