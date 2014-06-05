package sur.snapps.jetta.database.dummy.expression;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;


public class DateExpressionTest {

    private static final DateFormat FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    @Test
    public void testParseExpressionPlusTwice() {
        DateExpression expression = new DateExpression("${today + month(1) + day(7)}");

        Date result = expression.parse();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, 7);
        assertEquals(FORMAT.format(cal.getTime()), FORMAT.format(result));
    }

    @Test
    public void testParseExpressionPlus() {
        DateExpression expression = new DateExpression("${today + month(1)}");

        Date result = expression.parse();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        assertEquals(FORMAT.format(cal.getTime()), FORMAT.format(result));
    }

    @Test
    public void testParseExpressionToday() {
        DateExpression expression = new DateExpression("${today}");
        
        Date result = expression.parse();

        assertEquals(FORMAT.format(Calendar.getInstance().getTime()), FORMAT.format(result));
    }
    
    @Test
    public void testParseDateFormat1() {
        DateExpression expression = new DateExpression("05/06/2015");

        Date result = expression.parse();

        assertEquals("05-06-2015", FORMAT.format(result));
    }
    
    @Test
    public void testParseDateFormat2() {
        DateExpression expression = new DateExpression("05-06-2015");

        Date result = expression.parse();

        assertEquals("05-06-2015", FORMAT.format(result));
    }
    
    @Test
    public void testParseDateFormat3() {
        DateExpression expression = new DateExpression("2015-06-05");

        Date result = expression.parse();

        assertEquals("05-06-2015", FORMAT.format(result));
    }
    
    @Test
    public void testParseDateFormat4() {
        DateExpression expression = new DateExpression("2015/06/05");

        Date result = expression.parse();

        assertEquals("05-06-2015", FORMAT.format(result));
    }
}
