package sur.snapps.jetta.core.rules;

import sur.snapps.jetta.core.JettaModule;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 9:28
 */
public abstract class JettaRuleModule extends JettaModule {

    public abstract void init(Object target, String testName);

    public abstract void quit(boolean success);
}
