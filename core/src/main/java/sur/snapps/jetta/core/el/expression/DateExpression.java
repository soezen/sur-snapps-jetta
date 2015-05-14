package sur.snapps.jetta.core.el.expression;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import sur.snapps.jetta.core.el.operation.Operation;
import sur.snapps.jetta.core.el.operation.calendar.AddToCalendarOperationFactory;
import sur.snapps.jetta.core.el.operation.calendar.MinToCalendarOperationFactory;
import sur.snapps.jetta.core.el.operation.calendar.OperationFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class DateExpression {

    private String expression;
    
    private Expression<Calendar> dateExpression;
    private List<Operation<Calendar>> dateOperations;

    // TODO keep this in other class
    private Map<String, OperationFactory<Calendar>> operations;
    
    public DateExpression(String expression) {
        this.expression = expression.trim();
        dateOperations = Lists.newLinkedList();
        operations = Maps.newHashMap();
        operations.put("+", new AddToCalendarOperationFactory());
        operations.put("-", new MinToCalendarOperationFactory());
    }

    private List<Integer> indexesOfOperators(String expression) {
        List<Integer> indexes = Lists.newArrayList();
        for (String character : operations.keySet()) {
            int index = 0;
            while ((index = expression.indexOf(character, index + 1)) != -1) {
                indexes.add(index);
            }
        }
        return indexes;
    }
    
    public Date parse() {
        if (expression.startsWith("${") && expression.endsWith("}")) {
            expression = expression.substring(2, expression.length() - 1).trim();
            List<Integer> indexes = Ordering.natural().sortedCopy(indexesOfOperators(expression));

            if (indexes.isEmpty()) {
                dateExpression = new SingleDateExpression(expression);
            } else {
                int fromIndex = 0;
                int toIndex = 0;

                for (Integer index : indexes) {
                    fromIndex = toIndex;
                    toIndex = index;
                    if (dateExpression == null) {
                        dateExpression = new SingleDateExpression(expression.substring(fromIndex, toIndex).trim());
                    } else {
                        addOperation(fromIndex, toIndex);
                    }
                }

                addOperation(toIndex, expression.length());
            }
        } else {
            dateExpression = new SingleDateExpression(expression);
        }

        return calculate();
    }

    private void addOperation(int fromIndex, int toIndex) {
        String subExpression = expression.substring(fromIndex, toIndex).trim();
        String operator = subExpression.substring(0, 1);
        subExpression = subExpression.substring(1).trim();
        dateOperations.add(operations.get(operator).newInstance(subExpression));
    }

    private Date calculate() {
        Calendar cal = dateExpression.value();
        for (Operation<Calendar> operation : dateOperations) {
            cal = operation.perform(cal);
        }
        return cal.getTime();
    }
}
