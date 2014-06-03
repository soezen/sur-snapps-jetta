package sur.snapps.jetta.database.counter.expression.conditional;

import sur.snapps.jetta.database.counter.table.Column;

public class Conditionals {
    
    private static final String LIKE = " LIKE ";
    private static final String EQUAL = " = ";
    private static final String IS = " IS ";
    private static final String NULL = "NULL";
    
    private Conditionals() { }
    
    public static Conditional like(Column column, String value) {
        return new Conditional(column.name(), LIKE, value);
    }
    
    public static Conditional equal(Column column, String value) {
        return new Conditional(column.name(), EQUAL, value);
    }
    
    public static Conditional equal(Column column1, Column column2) {
        return new Conditional(column1.name(), EQUAL, column2.name());
    }
    
    public static Conditional isNull(Column column) {
        return new Conditional(column.name(), IS, NULL);
    }
}
