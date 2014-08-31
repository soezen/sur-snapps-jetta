package sur.snapps.jetta.selenium.module;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.common.Utils;
import com.saucelabs.saucerest.SauceREST;
import org.junit.runner.Description;
import sur.snapps.jetta.core.config.JettaConfigurations;
import sur.snapps.jetta.core.logger.JettaLogger;
import sur.snapps.jetta.core.watchers.JettaWatcherModule;

import java.util.HashMap;
import java.util.Map;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 14:13
 */
public class SauceModule extends JettaWatcherModule {

    private SeleniumConfiguration configuration = JettaConfigurations.get(SeleniumConfiguration.class);
    private SauceREST sauceREST;

    public SauceModule() {
        if (configuration.sauceActivated()) {
            JettaLogger.info(this.getClass(), "ACTIVATED");
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

            JettaLogger.info(this.getClass(), "Job link: " + sauceREST.getPublicJobLink(sessionIdProvider.getSessionId()));
        }
    }
}
