package sur.snapps.jetta.database.counter.expression.operator;

import sur.snapps.jetta.database.counter.expression.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class MultipleExpressionOperator implements Expression {

    private String keyword;
    private List<Expression> expressions;
    
    public MultipleExpressionOperator(String keyword, Expression expression1, Expression expression2, Expression...expressions) {
        this.keyword = keyword;
        this.expressions = new ArrayList<>();
        this.expressions.add(expression1);
        this.expressions.add(expression2);
        this.expressions.addAll(Arrays.asList(expressions));
    }
    
    @Override
    public String expression() {
        StringBuilder orExpression = new StringBuilder("(");

        Iterator<Expression> it = expressions.iterator();
        while (it.hasNext()) {
            Expression expression = it.next();
            orExpression.append(expression.expression())
                .append(it.hasNext() ? " " +  keyword + " " : "");
        }
        
        return orExpression.append(")").toString();
    }
}
