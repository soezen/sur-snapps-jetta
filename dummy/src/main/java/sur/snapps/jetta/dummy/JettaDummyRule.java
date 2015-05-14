package sur.snapps.jetta.dummy;

import sur.snapps.jetta.core.rules.JettaTestRule;
import sur.snapps.jetta.dummy.annotation.Dummies;
import sur.snapps.jetta.dummy.module.DummyModule;

/**
 * @author sur
 * @since 20/02/2015
 */
public class JettaDummyRule extends JettaTestRule<DummyModule> {

    private Object target;

    public JettaDummyRule(Object target) {
        super(target, DummyModule.getInstance());
        this.target = target;
    }

    public <D> D get(Class<D> dummyClass, String identifier, String value) {
        return DummyCollection
            .with(target.getClass().getAnnotation(Dummies.class))
            .find(dummyClass)
            .where(identifier)
            .is(value);
    }
}
