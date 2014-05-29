package sur.snapps.jetta.modules.selenium;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * User: SUR
 * Date: 29/05/14
 * Time: 16:29
 */
public class SeleniumTestRule implements TestRule {

    private Object target;

    public SeleniumTestRule(Object target) {
        this.target = target;
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                SeleniumModule.init(target);
                try {
                    base.evaluate();
                } finally {
                    SeleniumModule.quit();
                }
            }
        };
    }
}
