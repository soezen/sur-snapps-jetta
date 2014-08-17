package sur.snapps.jetta.database.counter.clause;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.annotation.Mock;
import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.expression.Expression;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.unitils.easymock.EasyMockUnitils.replay;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class WhereClauseTest {

    @Mock
    private Expression expression;
    
    @Test
    public void testGet() {
        expect(expression.expression(DatabaseDialect.MYSQL)).andReturn("expr");
        replay();
        
        WhereClause result = new WhereClause(expression);
        
        assertEquals(" WHERE expr", result.get(DatabaseDialect.MYSQL));
    }
}
