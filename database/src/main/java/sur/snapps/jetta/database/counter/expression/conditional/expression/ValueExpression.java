package sur.snapps.jetta.database.counter.expression.conditional.expression;

import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.expression.conditional.expression.ConditionExpression;

/**
 * User: SUR
 * Date: 15/08/14
 * Time: 18:26
 */
public class ValueExpression implements ConditionExpression {

    private String value;

    public ValueExpression(String value) {
        this.value = value;
    }

    @Override
    public String expression(DatabaseDialect dialect) {
        return value;
    }
}
