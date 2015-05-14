package sur.snapps.jetta.database.counter.clause;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.table.Table;

import static org.junit.Assert.assertEquals;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SelectClauseTest {

    @Mock
    private Table table;
    
    @Test
    public void testGetWithAlias() {
        EasyMock.expect(table.alias()).andReturn("a").times(2);
        EasyMock.expect(table.primaryKey()).andReturn("pk");
        EasyMockUnitils.replay();
        
        SelectClause result = new SelectClause(table);
        
        assertEquals("SELECT count(a.pk) as C", result.get(DatabaseDialect.MYSQL));
    }
    
    @Test
    public void testGetWithoutAlias() {
        EasyMock.expect(table.alias()).andReturn(null);
        EasyMockUnitils.replay();
        
        SelectClause result = new SelectClause(table);
        
        assertEquals("SELECT count(*) as C", result.get(DatabaseDialect.MYSQL));
    }
    
}
