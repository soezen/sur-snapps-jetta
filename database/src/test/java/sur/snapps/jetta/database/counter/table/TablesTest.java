package sur.snapps.jetta.database.counter.table;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;



public class TablesTest {

    @Test
    public void testTableWithNameOnly() {
        Table table = Tables.table("name");
        
        assertNotNull(table);
        assertEquals("name", table.name());
    }
    
    @Test
    public void testTable() {
        Table table = Tables.table("name", "a", "id");
        
        assertNotNull(table);
        assertEquals("name a", table.name());
        assertEquals("a", table.alias());
        assertEquals("id", table.primaryKey());
    }
    
    @Test
    public void constructorIsPrivate() throws Exception {
        Constructor<?> constructor = Tables.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
