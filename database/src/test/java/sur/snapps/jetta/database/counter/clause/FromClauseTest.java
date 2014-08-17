package sur.snapps.jetta.database.counter.clause;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.annotation.Mock;
import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.table.Table;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.unitils.easymock.EasyMockUnitils.replay;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class FromClauseTest {

    @Mock
    private Table table;
    
    @Test
    public void testGet() {
        expect(table.nameWithJoins()).andReturn("table");
        replay();
                       
        FromClause fromClause = new FromClause(table);
        
        assertEquals(" FROM table", fromClause.get(DatabaseDialect.MYSQL));
    }
}
