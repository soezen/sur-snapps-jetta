package sur.snapps.jetta.database.counter.expression.conditional.expression;

import sur.snapps.jetta.database.DatabaseDialect;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: SUR
 * Date: 15/08/14
 * Time: 18:26
 */
public class DateValueExpression implements ConditionExpression {

    private Date value;

    public DateValueExpression(Date value) {
        this.value = value;
    }

    @Override
    public String expression(DatabaseDialect dialect) {
        return "'" + new SimpleDateFormat("dd-MM-yyyy").format(value) + "'";
    }
}
