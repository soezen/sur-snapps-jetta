package sur.snapps.jetta.database.counter.expression.operator;

import sur.snapps.jetta.database.counter.expression.Expression;


public class Operators {

    private static final String AND = "AND";
    private static final String OR = "OR";
    private static final String NOT = "NOT";
    
    private Operators() { }
    
    public static Expression and(Expression expression1, Expression expression2, Expression...expressions) {
        return new MultipleExpressionOperator(AND, expression1, expression2, expressions);
    }
    
    public static Expression or(Expression expression1, Expression expression2, Expression...expressions) {
        return new MultipleExpressionOperator(OR, expression1, expression2, expressions);
    }
    
    public static Expression not(Expression expression) {
        return new SingleExpressionOperator(NOT, expression);
    }
}
