package sur.snapps.jetta.data.util;

import com.google.common.base.Predicate;
import sur.snapps.jetta.data.meta.JettaTable;

/**
 * @author sur
 * @since 07/03/2015
 */
public class Predicates {

    public static Predicate<JettaTable> tableWithName(String tableName) {
        return new TableWithNamePredicate(tableName);
    }

    static class TableWithNamePredicate implements Predicate<JettaTable> {

        private String tableName;

        public TableWithNamePredicate(String tableName) {
            this.tableName = tableName;
        }

        @Override
        public boolean apply(JettaTable table) {
            return table.tableName().equals(tableName);
        }
    }
}
