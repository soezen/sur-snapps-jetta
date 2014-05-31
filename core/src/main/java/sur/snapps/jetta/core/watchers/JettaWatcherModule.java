package sur.snapps.jetta.core.watchers;

import org.junit.runner.Description;
import sur.snapps.jetta.core.JettaModule;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 9:28
 */
public abstract class JettaWatcherModule extends JettaModule {

    protected JettaWatcherModule() {
        super();
    }

    protected abstract void succeeded(Object target, Description description);

    protected abstract void failed(Object target, Throwable throwable, Description description);

}
