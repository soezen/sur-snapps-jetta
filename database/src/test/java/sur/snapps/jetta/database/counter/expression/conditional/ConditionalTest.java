package sur.snapps.jetta.database.counter.expression.conditional;

import org.junit.Test;
import sur.snapps.jetta.database.DatabaseDialect;

import static org.junit.Assert.assertEquals;
import static sur.snapps.jetta.database.counter.expression.conditional.expression.ConditionExpressions.value;


public class ConditionalTest {
    
    @Test
    public void testExpression() {
        Conditional conditional = new Conditional(value("right"), Comparator.EQUAL, value("left"));
        
        assertEquals("right = left", conditional.expression(DatabaseDialect.MYSQL));
    }
}
