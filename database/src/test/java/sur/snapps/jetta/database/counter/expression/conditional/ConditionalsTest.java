package sur.snapps.jetta.database.counter.expression.conditional;

import org.junit.Test;
import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.table.Column;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ConditionalsTest {

    @Test
    public void testLike() {
        Column column = new Column("a", "column");
        Conditional result = Conditionals.like(column, "value");
        
        assertEquals("a.column LIKE value", result.expression(DatabaseDialect.MYSQL));
    }

    @Test
    public void testEqualWithValue() {
        Column column = new Column("a", "column");
        Conditional result = Conditionals.equal(column, "value");
        
        assertEquals("a.column = value", result.expression(DatabaseDialect.MYSQL));
    }

    @Test
    public void testEqualWithColumn() {
        Column column = new Column("a", "column");
        Conditional result = Conditionals.equal(column, column);
        
        assertEquals("a.column = a.column", result.expression(DatabaseDialect.MYSQL));
    }

    @Test
    public void testIsNull() {
        Column column = new Column("a", "column");
        Conditional result = Conditionals.isNull(column);
        
        assertEquals("a.column IS NULL", result.expression(DatabaseDialect.MYSQL));
    }
    
    @Test
    public void constructorIsPrivate() throws Exception {
        Constructor<?> constructor = Conditionals.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
