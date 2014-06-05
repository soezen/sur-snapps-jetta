package sur.snapps.jetta.database.dummy.expression;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.Map;


public class SingleDateExpression implements Expression<Calendar> {

    private String expression;
    private Deque<String> dateFormats;
    private Map<String, Calendar> keywords;
    
    public SingleDateExpression(String expression) {
        this.expression = expression;
        
        dateFormats = Lists.newLinkedList();
        dateFormats.add("dd-MM-yyyy");
        dateFormats.add("dd/MM/yyyy");
        dateFormats.add("yyyy-MM-dd");
        dateFormats.add("yyyy/MM/dd");
        
        keywords = Maps.newHashMap();
        keywords.put("today", Calendar.getInstance());
    }
    
    private Calendar getDateWithFormat(String formatExpression) {
        Date date = null;
        DateFormat format = new SimpleDateFormat(formatExpression);
        format.setLenient(false);
        try {
            date = format.parse(expression);
        } catch (ParseException e) {
            if (!dateFormats.isEmpty()) {
                return getDateWithFormat(dateFormats.pop());
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @Override
    public Calendar value() {
        if (keywords.containsKey(expression)) {
            return keywords.get(expression);
        }
        return getDateWithFormat(dateFormats.pop());
    }
}
