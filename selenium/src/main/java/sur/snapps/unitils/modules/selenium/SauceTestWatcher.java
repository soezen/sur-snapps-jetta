package sur.snapps.unitils.modules.selenium;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.common.Utils;
import com.saucelabs.saucerest.SauceREST;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.HashMap;
import java.util.Map;

public class SauceTestWatcher extends TestWatcher {

    private final SauceOnDemandSessionIdProvider target;
    private SauceREST sauceREST;
    private boolean verboseMode = true;
    private boolean activated;

    public SauceTestWatcher(SauceOnDemandSessionIdProvider target) {
        this(target, false);
    }

    public SauceTestWatcher(SauceOnDemandSessionIdProvider target, boolean verboseMode) {
        this.target = target;
        this.verboseMode = verboseMode;
        // TODO make configuration singleton
        SeleniumConfiguration configuration = new SeleniumConfiguration();
        activated = configuration.sauceActivated();
    }

    private SauceREST sauceREST() {
        if (sauceREST == null) {
            SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication();
            sauceREST = new SauceREST(authentication.getUsername(), authentication.getAccessKey());
        }
        return sauceREST;
    }

    /**
     * Invoked if the unit test passes without error or failure.  Invokes the Sauce REST API to mark the Sauce Job
     * as 'passed'.
     *
     * @param description not used
     */
    protected void succeeded(Description description) {
        if (activated && target.getSessionId() != null) {
            //log the session id to the system out
            printSessionId(description);
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("passed", true);
            Utils.addBuildNumberToUpdate(updates);
            sauceREST().updateJobInfo(target.getSessionId(), updates);
        }
    }

    private void printSessionId(Description description) {
        if (verboseMode) {
            String message = String.format("SauceOnDemandSessionID=%1$s job-name=%2$s.%3$s", target.getSessionId(), description.getClassName(), description.getMethodName());
            System.out.println(message);
        }
    }

    /**
     * Invoked if the unit test either throws an error or fails.  Invokes the Sauce REST API to mark the Sauce Job
     * as 'failed'.
     *
     * @param e           not used
     * @param description not used
     */
    protected void failed(Throwable e, Description description) {
        if (activated && target != null && target.getSessionId() != null) {
            printSessionId(description);
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("passed", false);
            Utils.addBuildNumberToUpdate(updates);
            sauceREST().updateJobInfo(target.getSessionId(), updates);

            if (verboseMode) {
                // get, and print to StdOut, the link to the job
                String authLink = sauceREST().getPublicJobLink(target.getSessionId());
                System.out.println("Job link: " + authLink);
            }
        }
    }


}
