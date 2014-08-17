package sur.snapps.jetta.database.counter.expression.operation;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.expression.Expression;

import static org.junit.Assert.assertEquals;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class MultipleOperationsExpressionTest {

    @Mock
    private Expression expression;
    
    @Test
    public void testExpression() {
        EasyMock.expect(expression.expression(DatabaseDialect.MYSQL)).andReturn("expression").times(4);
        EasyMockUnitils.replay();
        
        Expression result = new MultipleOperationsExpression("KEY", expression, expression, expression, expression);
        
        assertEquals("(expression KEY expression KEY expression KEY expression)", result.expression(DatabaseDialect.MYSQL));
    }
}
