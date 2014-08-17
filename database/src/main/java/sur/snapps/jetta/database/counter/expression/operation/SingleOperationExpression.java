package sur.snapps.jetta.database.counter.expression.operation;


import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.expression.Expression;

public class SingleOperationExpression implements Expression {

    private String keyword;
    private Expression expression;
    
    public SingleOperationExpression(String keyword, Expression expression) {
        this.keyword = keyword;
        this.expression = expression;
    }
    
    @Override
    public String expression(DatabaseDialect dialect) {
        return new StringBuilder(keyword)
            .append(" (")
            .append(expression.expression(dialect))
            .append(")")
            .toString();
    }
}
