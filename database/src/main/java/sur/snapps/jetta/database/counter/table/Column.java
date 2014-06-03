package sur.snapps.jetta.database.counter.table;


public class Column {

    private String table;
    private String column;
    
    public Column(String table, String column) {
        this.table = table;
        this.column = column;
    }
    
    public String name() {
        return new StringBuilder(table == null ? "" : (table + "."))
            .append(column)
            .toString();
    }
}
