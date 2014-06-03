package sur.snapps.jetta.database.counter.clause;


import sur.snapps.jetta.database.counter.table.Table;

public class FromClause implements Clause {

    private Table table;
    
    public FromClause(Table table) {
        this.table = table;
    }
    
    @Override
    public String get() {
        return new StringBuilder(" FROM ").append(table.nameWithJoins()).toString();
    }
}
