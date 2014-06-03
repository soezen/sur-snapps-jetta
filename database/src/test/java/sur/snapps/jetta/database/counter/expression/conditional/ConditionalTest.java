package sur.snapps.jetta.database.counter.expression.conditional;

import static org.junit.Assert.assertEquals;

import org.junit.Test;



public class ConditionalTest {
    
    @Test
    public void testExpression() {
        Conditional conditional = new Conditional("right", "condition", "left");
        
        assertEquals("rightconditionleft", conditional.expression());
    }
}
