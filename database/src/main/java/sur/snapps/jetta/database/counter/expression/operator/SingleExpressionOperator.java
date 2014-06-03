package sur.snapps.jetta.database.counter.expression.operator;


import sur.snapps.jetta.database.counter.expression.Expression;

public class SingleExpressionOperator implements Expression {

    private String keyword;
    private Expression expression;
    
    public SingleExpressionOperator(String keyword, Expression expression) {
        this.keyword = keyword;
        this.expression = expression;
    }
    
    @Override
    public String expression() {
        return new StringBuilder(keyword)
            .append(" (")
            .append(expression.expression())
            .append(")")
            .toString();
    }
}
