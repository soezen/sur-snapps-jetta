package sur.snapps.jetta.database.counter.table;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class Table {

    private String name;
    private String alias;
    private String primaryKey;
    
    private Map<Table, String> joinTables;
    
    public Table(String name) {
        this.name = name;
        joinTables = new HashMap<Table, String>();
    }
    
    public Table(String name, String alias, String primaryKey) {
        this(name);
        this.alias = alias;
        this.primaryKey = primaryKey;
    }
    
    public String alias() {
        return alias;
    }
    
    public String primaryKey() {
        return primaryKey;
    }
    
    public String name() {
        return new StringBuilder(name)
            .append(alias != null ? (" " + alias) : "")
            .toString();
    }
    
    public String nameWithJoins() {
        return new StringBuilder(name())
            .append(joinTables.isEmpty() ? "" : joins())
            .toString();
    }
    
    public String joins() {
        StringBuilder joinBuilder = new StringBuilder();
        
        for (Entry<Table, String> entry : joinTables.entrySet()) {
            String foreignKey = entry.getValue();
            Table joinTable = entry.getKey();
            joinBuilder.append(" JOIN ")
                .append(joinTable.name())
                .append(" ON ")
                .append(alias).append(".").append(primaryKey)
                .append(" = ")
                .append(joinTable.alias()).append(".").append(foreignKey);
            joinBuilder.append(joinTable.joins());
            
        }
        return joinBuilder.toString();
    }
    
    public Column column(String name) {
        return new Column(alias, name);
    }
    
    public Table join(Table joinTable, String foreignKey) {
        joinTables.put(joinTable, foreignKey);
        return this;
    }
}
