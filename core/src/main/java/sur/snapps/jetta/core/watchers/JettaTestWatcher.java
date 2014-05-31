package sur.snapps.jetta.core.watchers;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 9:28
 *
 * @param <M> JettaWatcherModule
 */
public class JettaTestWatcher<M extends JettaWatcherModule> extends TestWatcher {

    private Object target;
    private JettaWatcherModule module;

    public JettaTestWatcher(Object target, M module) {
        this.target = target;
        this.module = module;
    }

    @Override
    protected void succeeded(Description description) {
        module.succeeded(target, description);
    }

    @Override
    protected void failed(Throwable e, Description description) {
        module.failed(target, e, description);
    }
}
