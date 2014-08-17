package sur.snapps.jetta.database.counter.expression.conditional.expression;

import sur.snapps.jetta.database.counter.table.Column;

import java.util.Date;

/**
 * User: SUR
 * Date: 15/08/14
 * Time: 18:44
 */
public class ConditionExpressions {

    public static ConditionExpression column(Column column) {
        return new ColumnExpression(column);
    }

    public static ConditionExpression dateColumn(Column column) {
        return new DateColumnExpression(column);
    }

    public static ConditionExpression empty() {
        return new NullExpression();
    }

    public static ConditionExpression value(String value) {
        return new ValueExpression(value);
    }

    public static ConditionExpression dateValue(Date date) {
        return new DateValueExpression(date);
    }
}
