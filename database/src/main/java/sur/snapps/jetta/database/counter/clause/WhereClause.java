package sur.snapps.jetta.database.counter.clause;


import sur.snapps.jetta.database.counter.expression.Expression;

public class WhereClause implements Clause {

    private Expression expression;
    
    public WhereClause(Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public String get() {
        return " WHERE " + expression.expression();
    }
    
}
