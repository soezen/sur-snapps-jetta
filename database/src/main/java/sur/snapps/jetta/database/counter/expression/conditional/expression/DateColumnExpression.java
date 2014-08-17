package sur.snapps.jetta.database.counter.expression.conditional.expression;

import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.expression.Functions;
import sur.snapps.jetta.database.counter.table.Column;

/**
 * User: SUR
 * Date: 15/08/14
 * Time: 18:26
 */
public class DateColumnExpression implements ConditionExpression {

    private Column column;

    public DateColumnExpression(Column column) {
        this.column = column;
    }

    @Override
    public String expression(DatabaseDialect dialect) {
        return Functions.dateColumnToString(dialect, column.name());
    }
}
