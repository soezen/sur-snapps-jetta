package sur.snapps.jetta.database.counter.clause;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import sur.snapps.jetta.database.counter.expression.Expression;

import static org.junit.Assert.assertEquals;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class WhereClauseTest {

    @Mock
    private Expression expression;
    
    @Test
    public void testGet() {
        EasyMock.expect(expression.expression()).andReturn("expr");
        EasyMockUnitils.replay();
        
        WhereClause result = new WhereClause(expression);
        
        assertEquals(" WHERE expr", result.get());
    }
}
