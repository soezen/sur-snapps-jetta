package sur.snapps.jetta.selenium;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import sur.snapps.jetta.core.watchers.JettaTestWatcher;
import sur.snapps.jetta.selenium.module.SauceModule;

public class SauceTestWatcher extends JettaTestWatcher<SauceModule> {

    public SauceTestWatcher(SauceOnDemandSessionIdProvider target) {
        super(target, new SauceModule());
    }

}
