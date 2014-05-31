package sur.snapps.jetta.core.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 9:28
 *
 * @param <M> JettaRuleModule
 */
public class JettaTestRule<M extends JettaRuleModule> implements TestRule {

    private Object target;
    private JettaRuleModule module;

    public JettaTestRule(Object target, M module) {
        this.target = target;
        this.module = module;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                module.init(target, description.getMethodName());
                try {
                    base.evaluate();
                } finally {
                    module.quit();
                }
            }
        };
    }
}
