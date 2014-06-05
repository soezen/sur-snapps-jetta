package sur.snapps.jetta.database.dummy.operation.calendar;

import sur.snapps.jetta.database.dummy.operation.Operation;

import java.util.Calendar;

/**
 * User: SUR
 * Date: 5/06/14
 * Time: 21:57
 */
public class AddToCalendarOperationFactory implements OperationFactory<Calendar> {

    @Override
    public Operation<Calendar> newInstance(String expression) {
        return new AddToCalendarOperation(expression);
    }
}
