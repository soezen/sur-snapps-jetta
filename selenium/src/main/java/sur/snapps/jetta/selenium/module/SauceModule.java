package sur.snapps.jetta.selenium.module;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.common.Utils;
import com.saucelabs.saucerest.SauceREST;
import org.junit.runner.Description;
import sur.snapps.jetta.core.watchers.JettaWatcherModule;

import java.util.HashMap;
import java.util.Map;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 14:13
 */
public class SauceModule extends JettaWatcherModule {

    private SeleniumConfiguration configuration;
    private SauceREST sauceREST;

    public SauceModule() {
        if (configuration.sauceActivated()) {
            sauceREST = new SauceREST(configuration.sauceUsername(), configuration.sauceApiKey());
        }
    }

    @Override
    protected void succeeded(Object target, Description description) {
        SauceOnDemandSessionIdProvider sessionIdProvider = (SauceOnDemandSessionIdProvider) target;
        if (configuration.sauceActivated() && sessionIdProvider.getSessionId() != null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("passed", true);
            Utils.addBuildNumberToUpdate(updates);
            sauceREST.updateJobInfo(sessionIdProvider.getSessionId(), updates);
        }
    }

    @Override
    protected void failed(Object target, Throwable throwable, Description description) {
        SauceOnDemandSessionIdProvider sessionIdProvider = (SauceOnDemandSessionIdProvider) target;
        if (configuration.sauceActivated()
                && sessionIdProvider != null && sessionIdProvider.getSessionId() != null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("passed", false);
            Utils.addBuildNumberToUpdate(updates);
            sauceREST.updateJobInfo(sessionIdProvider.getSessionId(), updates);

            // TODO put in config
//            if (verboseMode) {
                // get, and print to StdOut, the link to the job
                String authLink = sauceREST.getPublicJobLink(sessionIdProvider.getSessionId());
                System.out.println("Job link: " + authLink);
//            }
        }
    }
}
