package sur.snapps.jetta.data;

import sur.snapps.jetta.core.rules.JettaTestRule;
import sur.snapps.jetta.data.module.DataModule;

/**
 * @author sur
 * @since 20/02/2015
 */
public class JettaDataRule extends JettaTestRule<DataModule> {

    private Object target;

    public JettaDataRule(Object target) {
        super(target, DataModule.getInstance());
        this.target = target;
    }

    public <D> D get(Class<D> dummyClass, String identifier, String value) {
//        return DataCollection
//            .find(dummyClass)
//            .where(identifier)
//            .is(value);
        return null;
    }
}
