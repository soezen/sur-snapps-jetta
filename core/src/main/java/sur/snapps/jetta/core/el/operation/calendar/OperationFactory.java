package sur.snapps.jetta.core.el.operation.calendar;

import sur.snapps.jetta.core.el.operation.Operation;

/**
 * User: SUR
 * Date: 5/06/14
 * Time: 21:56
 */
public interface OperationFactory<T> {

    Operation<T> newInstance(String expression);
}
