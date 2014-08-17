package sur.snapps.jetta.database.counter.expression.conditional;


import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.expression.Expression;
import sur.snapps.jetta.database.counter.expression.conditional.expression.ConditionExpression;

public class Conditional implements Expression {

    private ConditionExpression right;
    private Comparator comparator;
    private ConditionExpression left;
    
    public Conditional(ConditionExpression right, Comparator comparator, ConditionExpression left) {
        this.right = right;
        this.comparator = comparator;
        this.left = left;
    }
    
    @Override
    public String expression(DatabaseDialect dialect) {
        return right.expression(dialect) + comparator.get() + left.expression(dialect);
    }
}
