package sur.snapps.jetta.database.counter.expression.conditional;

import sur.snapps.jetta.database.counter.table.Column;

import java.util.Date;

import static sur.snapps.jetta.database.counter.expression.conditional.expression.ConditionExpressions.column;
import static sur.snapps.jetta.database.counter.expression.conditional.expression.ConditionExpressions.dateColumn;
import static sur.snapps.jetta.database.counter.expression.conditional.expression.ConditionExpressions.dateValue;
import static sur.snapps.jetta.database.counter.expression.conditional.expression.ConditionExpressions.empty;
import static sur.snapps.jetta.database.counter.expression.conditional.expression.ConditionExpressions.value;

public class Conditionals {
    

    
    private Conditionals() { }
    
    public static Conditional like(Column column, String value) {
        return new Conditional(column(column), Comparator.LIKE, value(value));
    }
    
    public static Conditional equal(Column column, String value) {
        return new Conditional(column(column), Comparator.EQUAL, value(value));
    }

    public static Conditional equalDate(Column column, Date value) {
        return new Conditional(dateColumn(column), Comparator.EQUAL, dateValue(value));
    }
    
    public static Conditional equal(Column column1, Column column2) {
        return new Conditional(column(column1), Comparator.EQUAL, column(column2));
    }
    
    public static Conditional isNull(Column column) {
        return new Conditional(column(column), Comparator.IS, empty());
    }
}
