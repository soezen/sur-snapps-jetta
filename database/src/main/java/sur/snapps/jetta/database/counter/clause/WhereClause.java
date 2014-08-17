package sur.snapps.jetta.database.counter.clause;


import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.expression.Expression;

public class WhereClause implements Clause {

    private Expression expression;
    
    public WhereClause(Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public String get(DatabaseDialect dialect) {
        return " WHERE " + expression.expression(dialect);
    }
    
}
