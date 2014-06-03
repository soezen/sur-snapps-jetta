package sur.snapps.jetta.database.counter.table;

import org.junit.Test;
import sur.snapps.jetta.database.counter.table.Column;

import static org.junit.Assert.assertEquals;


public class ColumnTest {
    
    @Test
    public void testNameWithAlias() {
        Column column = new Column("a", "name");
        
        assertEquals("a.name", column.name());
    }
    
    @Test
    public void testNameWithoutAlias() {
        Column column = new Column(null, "name");
        
        assertEquals("name", column.name());
    }

}
