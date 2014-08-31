package sur.snapps.jetta.core.rules;

import com.google.common.base.Stopwatch;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import sur.snapps.jetta.core.TestResult;

import java.util.concurrent.TimeUnit;

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
    private Stopwatch stopwatch;

    public JettaTestRule(Object target, M module) {
        this.target = target;
        this.module = module;
        stopwatch = Stopwatch.createUnstarted();
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                boolean canRunTest = module.init(target, description);
                TestResult result = TestResult.skippedResult();

                try {
                    if (canRunTest) {
                        stopwatch.start();
                        base.evaluate();
                        stopwatch.stop();
                        result = TestResult.successResult(stopwatch.elapsed(TimeUnit.MILLISECONDS));
                    }
                } catch (Throwable throwable) {
                    stopwatch.stop();
                    result = TestResult.failureResult(throwable, stopwatch.elapsed(TimeUnit.MILLISECONDS));
                    throw throwable;
                } finally {
                    module.quit(result);
                }
            }
        };
    }
}
