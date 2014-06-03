package sur.snapps.jetta.database.counter.expression.conditional;


import sur.snapps.jetta.database.counter.expression.Expression;

public class Conditional implements Expression {

    private String right;
    private String condition;
    private String left;
    
    public Conditional(String right, String condition, String left) {
        this.right = right;
        this.condition = condition;
        this.left = left;
    }
    
    @Override
    public String expression() {
        return right + condition + left;
    }
}
