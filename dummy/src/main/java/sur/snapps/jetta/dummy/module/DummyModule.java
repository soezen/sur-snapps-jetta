package sur.snapps.jetta.dummy.module;

import org.junit.runner.Description;
import org.unitils.util.ReflectionUtils;
import sur.snapps.jetta.core.TestResult;
import sur.snapps.jetta.core.logger.JettaLogger;
import sur.snapps.jetta.core.rules.JettaRuleModule;
import sur.snapps.jetta.dummy.DummyCollection;
import sur.snapps.jetta.dummy.annotation.Dummies;
import sur.snapps.jetta.dummy.annotation.Dummy;

import java.lang.reflect.Field;
import java.util.Set;

import static org.unitils.util.ReflectionUtils.setFieldValue;

/**
 * Dummy module that will look for fields annotated with @Dummy and
 * then inject them with the value created by DummyCreator.
 *
 * @author sur
 * @since 20/02/2015
 */
public class DummyModule extends JettaRuleModule {

    private static DummyModule instance;
    private Dummies dummiesConfiguration;

    private DummyModule() {
    }

    public static DummyModule getInstance() {
        if (instance == null) {
            instance = new DummyModule();
        }
        return instance;
    }

    @Override
    public boolean init(Object target, Description description) {
        JettaLogger.info(this.getClass(), "ACTIVATED");
        initializeDummiesConfiguration(target);
        injectDummies(target);
        return true;
    }

    private void injectDummies(Object target) {
        Set<Field> fields = ReflectionUtils.getAllFields(target.getClass());
        for (Field field : fields) {
            Dummy dummyAnnotation = field.getAnnotation(Dummy.class);
            if (dummyAnnotation != null) {
                Object dummy = DummyCollection
                    .with(dummiesConfiguration)
                    .find(field.getType())
                    .where(dummyAnnotation.identifier())
                    .is(dummyAnnotation.value());
                setFieldValue(target, field, dummy);
            }
        }
    }

    private void initializeDummiesConfiguration(Object target) {
        dummiesConfiguration = target.getClass().getAnnotation(Dummies.class);
    }

    @Override
    public void quit(TestResult result) {

    }
}
