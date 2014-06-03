package sur.snapps.jetta.database.counter.table;


public class Tables {

    private Tables() { }
    
    public static Table table(String name) {
        return new Table(name);
    }
    
    public static Table table(String name, String alias, String primaryKey) {
        return new Table(name, alias, primaryKey);
    }
}
