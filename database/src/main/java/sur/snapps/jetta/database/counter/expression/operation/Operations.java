package sur.snapps.jetta.database.counter.expression.operation;

import sur.snapps.jetta.database.counter.expression.Expression;


public class Operations {

    private static final String AND = "AND";
    private static final String OR = "OR";
    private static final String NOT = "NOT";
    
    private Operations() { }
    
    public static Expression and(Expression expression1, Expression expression2, Expression...expressions) {
        return new MultipleOperationsExpression(AND, expression1, expression2, expressions);
    }
    
    public static Expression or(Expression expression1, Expression expression2, Expression...expressions) {
        return new MultipleOperationsExpression(OR, expression1, expression2, expressions);
    }
    
    public static Expression not(Expression expression) {
        return new SingleOperationExpression(NOT, expression);
    }
}
