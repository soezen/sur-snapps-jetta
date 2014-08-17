package sur.snapps.jetta.database.counter.expression.operation;

import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.expression.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class MultipleOperationsExpression implements Expression {

    private String keyword;
    private List<Expression> expressions;
    
    public MultipleOperationsExpression(String keyword, Expression expression1, Expression expression2, Expression... expressions) {
        this.keyword = keyword;
        this.expressions = new ArrayList<>();
        this.expressions.add(expression1);
        this.expressions.add(expression2);
        this.expressions.addAll(Arrays.asList(expressions));
    }
    
    @Override
    public String expression(DatabaseDialect dialect) {
        StringBuilder orExpression = new StringBuilder("(");

        Iterator<Expression> it = expressions.iterator();
        while (it.hasNext()) {
            Expression expression = it.next();
            orExpression.append(expression.expression(dialect))
                .append(it.hasNext() ? " " +  keyword + " " : "");
        }
        
        return orExpression.append(")").toString();
    }
}
