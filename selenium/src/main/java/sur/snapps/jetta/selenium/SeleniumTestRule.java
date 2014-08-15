package sur.snapps.jetta.selenium;

import sur.snapps.jetta.core.rules.JettaTestRule;
import sur.snapps.jetta.selenium.module.SeleniumModule;

/**
 * User: SUR
 * Date: 29/05/14
 * Time: 16:29
 */
public class SeleniumTestRule extends JettaTestRule<SeleniumModule> {


    public SeleniumTestRule(Object target) {
        super(target, new SeleniumModule());
    }


}
