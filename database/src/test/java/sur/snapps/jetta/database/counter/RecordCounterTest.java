package sur.snapps.jetta.database.counter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.annotation.Mock;
import org.unitils.util.ReflectionUtils;
import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.clause.CountStatement;

import java.sql.Connection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class RecordCounterTest {

    @Mock
    private Connection connection;

    @Test
    public void testCount() {
        RecordCounter recordCounter = new RecordCounter(connection, DatabaseDialect.MYSQL);
        CountStatement result = recordCounter.count();
        
        assertNotNull(result);
        assertSame(connection, ReflectionUtils.getFieldValue(result, ReflectionUtils.getFieldWithName(CountStatement.class, "connection", false)));
    }
}
