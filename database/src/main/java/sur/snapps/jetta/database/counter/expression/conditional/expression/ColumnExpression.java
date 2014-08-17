package sur.snapps.jetta.database.counter.expression.conditional.expression;

import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.table.Column;

/**
 * User: SUR
 * Date: 15/08/14
 * Time: 18:26
 */
public class ColumnExpression implements ConditionExpression {

    private Column column;

    public ColumnExpression(Column column) {
        this.column = column;
    }

    @Override
    public String expression(DatabaseDialect dialect) {
        return column.name();
    }
}
