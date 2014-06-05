package sur.snapps.jetta.database.dummy.operation.calendar;

import sur.snapps.jetta.database.dummy.operation.Operation;

/**
 * User: SUR
 * Date: 5/06/14
 * Time: 21:56
 */
public interface OperationFactory<T> {

    Operation<T> newInstance(String expression);
}
