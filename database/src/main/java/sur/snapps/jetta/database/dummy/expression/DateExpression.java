package sur.snapps.jetta.database.dummy.expression;

import com.google.common.collect.Lists;
import sur.snapps.jetta.database.dummy.operation.Operation;
import sur.snapps.jetta.database.dummy.operation.calendar.AddToCalendarOperation;
import sur.snapps.jetta.database.dummy.operation.calendar.MinToCalendarOperation;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DateExpression {

    private String expression;
    
    private Expression<Calendar> dateExpression;
    private List<Operation<Calendar>> dateOperations;
    
    public DateExpression(String expression) {
        this.expression = expression.trim();
        dateOperations = Lists.newLinkedList();
    }
    
    public Date parse() {
        // TODO refactor
        if (expression.startsWith("${") && expression.endsWith("}")) {
            expression = expression.substring(2, expression.length() - 1).trim();
            int indexPlus = expression.indexOf("+");
            int indexMin = expression.indexOf("-");
            if (indexPlus == -1 && indexMin == -1) {
                dateExpression = new SingleDateExpression(expression);
            } else if (indexPlus != -1 && indexMin == -1) {
                String firstExpression = expression.substring(0, indexPlus).trim();
                dateExpression = new SingleDateExpression(firstExpression);
                if (expression.indexOf("+", indexPlus + 1) == -1) {
                    dateOperations.add(new AddToCalendarOperation(expression.substring(indexPlus + 1).trim()));
                }
            } else if (indexPlus == -1) {
                String firstExpression = expression.substring(0, indexMin).trim();
                dateExpression = new SingleDateExpression(firstExpression);
                if (expression.indexOf("-", indexMin + 1) == -1) {
                    dateOperations.add(new MinToCalendarOperation(expression.substring(indexMin + 1).trim()));
                }
            }
        } else {
            dateExpression = new SingleDateExpression(expression);
        }

        return calculate();
    }
    
    private Date calculate() {
        Calendar cal = dateExpression.value();
        for (Operation<Calendar> operation : dateOperations) {
            cal = operation.perform(cal);
        }
        return cal.getTime();
    }
}
