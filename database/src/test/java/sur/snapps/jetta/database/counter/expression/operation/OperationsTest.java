package sur.snapps.jetta.database.counter.expression.operation;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.expression.Expression;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class OperationsTest {

    @Mock
    private Expression expression;
    
    @Test
    public void testAnd() {
        EasyMock.expect(expression.expression(DatabaseDialect.MYSQL)).andReturn("expr").times(2);
        EasyMockUnitils.replay();
        
        Expression result = Operations.and(expression, expression);
        
        assertNotNull(result);
        assertEquals("(expr AND expr)", result.expression(DatabaseDialect.MYSQL));
    }

    @Test
    public void testOr() {
        EasyMock.expect(expression.expression(DatabaseDialect.MYSQL)).andReturn("expr").times(2);
        EasyMockUnitils.replay();
        
        Expression result = Operations.or(expression, expression);
        
        assertNotNull(result);
        assertEquals("(expr OR expr)", result.expression(DatabaseDialect.MYSQL));
    }

    @Test
    public void testNot() {
        EasyMock.expect(expression.expression(DatabaseDialect.MYSQL)).andReturn("expr");
        EasyMockUnitils.replay();
        
        Expression result = Operations.not(expression);
        
        assertNotNull(result);
        assertEquals("NOT (expr)", result.expression(DatabaseDialect.MYSQL));
    }
    
    @Test
    public void constructorIsPrivate() throws Exception {
        Constructor<?> constructor = Operations.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
