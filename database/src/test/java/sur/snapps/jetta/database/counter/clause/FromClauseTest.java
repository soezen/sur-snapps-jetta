package sur.snapps.jetta.database.counter.clause;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import sur.snapps.jetta.database.counter.table.Table;

import static org.junit.Assert.assertEquals;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class FromClauseTest {

    @Mock
    private Table table;
    
    @Test
    public void testGet() {
        EasyMock.expect(table.nameWithJoins()).andReturn("table");
        EasyMockUnitils.replay();
                       
        FromClause fromClause = new FromClause(table);
        
        assertEquals(" FROM table", fromClause.get());
    }
}
