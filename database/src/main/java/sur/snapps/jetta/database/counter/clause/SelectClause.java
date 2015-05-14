package sur.snapps.jetta.database.counter.clause;


import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.table.Table;

public class SelectClause implements Clause {

    private Table table;
    
    public SelectClause(Table table) {
        this.table = table;
    }
    
    @Override
    public String get(DatabaseDialect dialect) {
        return new StringBuilder("SELECT count(")
            .append(table.alias() == null ? "*" : (table.alias() + "." + table.primaryKey()))
            .append(") as C")
            .toString();
    }
}
