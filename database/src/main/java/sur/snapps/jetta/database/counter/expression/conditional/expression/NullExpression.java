package sur.snapps.jetta.database.counter.expression.conditional.expression;

import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.expression.conditional.expression.ConditionExpression;

/**
 * User: SUR
 * Date: 15/08/14
 * Time: 18:26
 */
public class NullExpression implements ConditionExpression {

    @Override
    public String expression(DatabaseDialect dialect) {
        return "NULL";
    }
}
