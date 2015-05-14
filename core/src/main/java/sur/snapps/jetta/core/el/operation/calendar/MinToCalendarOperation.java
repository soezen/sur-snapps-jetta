package sur.snapps.jetta.core.el.operation.calendar;

import java.util.Calendar;


public class MinToCalendarOperation extends CalendarOperation {
    
    private int field;
    private int amount;
    
    public MinToCalendarOperation(String expression) {
        super();
        field = getField(expression);
        amount = getAmount(expression);
    }
    
    @Override
    public Calendar perform(Calendar subject) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(subject.getTime());
        cal.add(field, - amount);
        return cal;
    }

}
