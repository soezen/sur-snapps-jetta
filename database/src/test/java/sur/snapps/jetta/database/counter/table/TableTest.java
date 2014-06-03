package sur.snapps.jetta.database.counter.table;

import org.junit.Test;
import sur.snapps.jetta.database.counter.table.Table;

import static org.junit.Assert.assertEquals;


public class TableTest {

    @Test
    public void testNameWithAlias() {
        Table table = new Table("name", "a", "id");
        
        assertEquals("name a", table.name());
    }
    
    @Test
    public void testNameWithoutAlias() {
        Table table = new Table("name");
        
        assertEquals("name", table.name());
    }
    
    @Test
    public void testNameWithJoins() {
        // TODO create mothers;
        Table table = new Table("one", "1", "id");
        Table joinTable = new Table("two", "2", "id");
        table.join(joinTable, "fk");
        
        assertEquals("one 1", table.name());
        assertEquals("one 1 JOIN two 2 ON 1.id = 2.fk", table.nameWithJoins());
    }
    
    @Test
    public void testNameWithJoinsWithoutJoins() {
        Table table = new Table("one", "1", "id");
        
        assertEquals("one 1", table.nameWithJoins());
    }
    
    @Test
    public void testColumnWithoutAlias() {
        Table table = new Table("name");
        Column column = table.column("column");

        assertEquals("column", column.name());
    }
    
    @Test
    public void testColumnWithAlias() {
        Table table = new Table("name", "a", "id");
        Column column = table.column("column");
        
        assertEquals("a.column", column.name());
    }
    
    @Test
    public void testConstructorsAndGetters() {
        Table table = new Table("name", "a", "id");

        assertEquals("a", table.alias());
        assertEquals("id", table.primaryKey());
    }
}
