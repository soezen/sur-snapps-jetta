package sur.snapps.jetta.core.rules;

import org.junit.runner.Description;
import sur.snapps.jetta.core.JettaModule;
import sur.snapps.jetta.core.TestResult;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 9:28
 */
public abstract class JettaRuleModule extends JettaModule {

    public abstract boolean init(Object target, Description description);

    public abstract void quit(TestResult result);
}
