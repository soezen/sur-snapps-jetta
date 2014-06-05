package sur.snapps.jetta.database.dummy.expression;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;


public class DateExpressionTest {

    @Test
    public void testParseExpressionToday() {
        DateExpression expression = new DateExpression("${today}");
        
        Date result = expression.parse();
        
        assertEquals(Calendar.getInstance().getTime(), result);
    }
    
    @Test
    public void testParseDateFormat1() {
        DateExpression expression = new DateExpression("05/06/2015");

        Date result = expression.parse();

        Calendar cal = Calendar.getInstance();
        cal.setTime(result);

        assertEquals(2015, cal.get(Calendar.YEAR));
        assertEquals(Calendar.JUNE, cal.get(Calendar.MONTH));
        assertEquals(5, cal.get(Calendar.DAY_OF_MONTH));
    }
    
    @Test
    public void testParseDateFormat2() {
        DateExpression expression = new DateExpression("05-06-2015");

        Date result = expression.parse();

        Calendar cal = Calendar.getInstance();
        cal.setTime(result);

        assertEquals(2015, cal.get(Calendar.YEAR));
        assertEquals(Calendar.JUNE, cal.get(Calendar.MONTH));
        assertEquals(5, cal.get(Calendar.DAY_OF_MONTH));
    }
    
    @Test
    public void testParseDateFormat3() {
        DateExpression expression = new DateExpression("2015-06-05");

        Date result = expression.parse();

        Calendar cal = Calendar.getInstance();
        cal.setTime(result);

        assertEquals(2015, cal.get(Calendar.YEAR));
        assertEquals(Calendar.JUNE, cal.get(Calendar.MONTH));
        assertEquals(5, cal.get(Calendar.DAY_OF_MONTH));
    }
    
    @Test
    public void testParseDateFormat4() {
        DateExpression expression = new DateExpression("2015/06/05");

        Date result = expression.parse();

        Calendar cal = Calendar.getInstance();
        cal.setTime(result);

        assertEquals(2015, cal.get(Calendar.YEAR));
        assertEquals(Calendar.JUNE, cal.get(Calendar.MONTH));
        assertEquals(5, cal.get(Calendar.DAY_OF_MONTH));
    }
}
