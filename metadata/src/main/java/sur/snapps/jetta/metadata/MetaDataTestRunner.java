package sur.snapps.jetta.metadata;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * User: SUR
 * Date: 25/08/14
 * Time: 18:40
 */
public class MetaDataTestRunner extends BlockJUnit4ClassRunner {


    public MetaDataTestRunner(Class<?> target) throws InitializationError {
        super(target);
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new MetaDataRunListener());
        super.run(notifier);
    }
}
