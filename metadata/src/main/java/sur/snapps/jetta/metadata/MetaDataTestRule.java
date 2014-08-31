package sur.snapps.jetta.metadata;

import sur.snapps.jetta.core.rules.JettaTestRule;
import sur.snapps.jetta.metadata.module.MetaDataModule;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 22:57
 */
public class MetaDataTestRule extends JettaTestRule<MetaDataModule> {

    public MetaDataTestRule(Object target) {
        super(target, MetaDataModule.getInstance());
    }
}
