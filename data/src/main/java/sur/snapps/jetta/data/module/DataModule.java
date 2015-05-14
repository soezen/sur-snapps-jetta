package sur.snapps.jetta.data.module;

import org.junit.runner.Description;
import sur.snapps.jetta.core.TestResult;
import sur.snapps.jetta.core.logger.JettaLogger;
import sur.snapps.jetta.core.rules.JettaRuleModule;
import sur.snapps.jetta.data.DataCollection;
import sur.snapps.jetta.data.DataObject;

import java.lang.reflect.Field;
import java.util.Set;

import static org.unitils.util.ReflectionUtils.getAllFields;
import static org.unitils.util.ReflectionUtils.setFieldValue;

/**
 * @author sur
 * @since 20/02/2015
 */
public class DataModule extends JettaRuleModule {

    private static DataModule instance;

    private DataModule() { }

    public static DataModule getInstance() {
        if (instance == null) {
            instance = new DataModule();
        }
        return instance;
    }

    @Override
    public boolean init(Object target, Description description) {
        JettaLogger.info(this.getClass(), "ACTIVATED");
        injectData(target);
        return true;
    }

    private void injectData(Object target) {
        Set<Field> fields = getAllFields(target.getClass());
        for (Field field : fields) {
            DataObject dataAnnotation = field.getAnnotation(DataObject.class);
            if (dataAnnotation != null) {
                Object dummy = DataCollection
                    .find(field.getType())
                    .where(dataAnnotation.identifier())
                    .is(dataAnnotation.value())
                    .singleResult();
                setFieldValue(target, field, dummy);
            }
        }
    }

    @Override
    public void quit(TestResult result) {

    }
}
