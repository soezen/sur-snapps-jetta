package sur.snapps.jetta.database;

import sur.snapps.jetta.core.rules.JettaTestRule;
import sur.snapps.jetta.database.module.DatabaseModule;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 22:57
 */
public class DatabaseTestRule extends JettaTestRule<DatabaseModule> {

    public DatabaseTestRule(Object target, DatabaseModule module) {
        super(target, module);
    }
}
