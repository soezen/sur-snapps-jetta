package sur.snapps.jetta.database.dummy.operation.calendar;

import com.google.common.collect.Maps;
import sur.snapps.jetta.database.dummy.operation.Operation;

import java.util.Calendar;
import java.util.Map;


public abstract class CalendarOperation implements Operation<Calendar> {
    
    private Map<String, Integer> fields;
    
    public CalendarOperation() {
        fields = Maps.newHashMap();
        fields.put("month", Calendar.MONTH);
        fields.put("day", Calendar.DAY_OF_MONTH);
    }

    protected int getField(String expression) {
        String fieldExpression = expression.substring(0, expression.indexOf("("));
        return fields.get(fieldExpression);
    }
    
    protected int getAmount(String expression) {
        String amount = expression.substring(expression.indexOf("(") + 1, expression.indexOf(")"));
        return Integer.valueOf(amount);
    }
}
