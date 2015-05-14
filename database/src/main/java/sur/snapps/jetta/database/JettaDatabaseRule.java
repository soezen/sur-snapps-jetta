package sur.snapps.jetta.database;

import sur.snapps.jetta.core.rules.JettaTestRule;
import sur.snapps.jetta.database.counter.RecordCounter;
import sur.snapps.jetta.database.module.DatabaseModule;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 22:57
 */
public class JettaDatabaseRule extends JettaTestRule<DatabaseModule> {

    public JettaDatabaseRule(Object target) {
        super(target, DatabaseModule.getInstance());
    }

    public RecordCounter recordCounter() {
        return DatabaseModule.getInstance().recordCounter();
    }
}
